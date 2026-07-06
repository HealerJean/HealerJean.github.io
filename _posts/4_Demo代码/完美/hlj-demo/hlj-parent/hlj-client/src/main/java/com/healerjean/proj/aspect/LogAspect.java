package com.healerjean.proj.aspect;

import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.config.LogConfiguration;
import com.healerjean.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 统一日志切面：基于 {@link LogIndex} 注解，对被标注的方法进行入参/返回值/耗时统一记录。
 * <p>
 * 核心职责：
 * <ul>
 *     <li>从目标类中反射获取业务自身的 Logger，保证日志归属正确。</li>
 *     <li>结合 {@link LogConfiguration} 的动态开关，决定是否打印请求/响应。</li>
 *     <li>识别慢调用（超过注解配置的预估耗时），打上 timeOut 标记。</li>
 * </ul>
 * 所属业务模块：通用基础设施 - 日志监控。
 *
 * @author zhangyujin
 * @date 2023/6/14 18:17
 */
@Slf4j
@Component
@Aspect
public class LogAspect {

    /**
     * 日志动态配置：支持运行时控制是否打印某个方法的入参/返回值
     */
    @Resource
    private LogConfiguration logConfiguration;

    /**
     * 环绕通知：拦截所有标注 {@link LogIndex} 的方法，统一打印业务日志。
     * <p>
     * 流程：获取目标 Logger → 反射解析 Method → 执行原方法 → 在 finally 中组装并输出日志。
     *
     * @param pjp      AspectJ 切入点，封装目标方法的调用上下文
     * @param logIndex 方法上的日志注解，提供业务标识、开关、预估耗时等元数据
     * @return 原方法的执行结果
     * @throws Throwable 原方法抛出的任意异常，原样向上抛出，不做吞噬
     */
    @Around(value = "(execution(* *(..)) && @annotation(logIndex))", argNames = "pjp,logIndex")
    public Object printLog(final ProceedingJoinPoint pjp, LogIndex logIndex) throws Throwable {

        // 1. 切面上下文准备与反射解析
        //    1) 入参数组、方法签名等基础上下文
        Object[] methodArgs = pjp.getArgs();
        Signature signature = pjp.getSignature();
        //    2) 通过反射定位真正的 Method，便于后续读取参数名等元数据
        Method targetMethod = null;
        if ((signature instanceof MethodSignature)) {
            MethodSignature methodSignature = (MethodSignature) signature;
            Object targetBean = pjp.getTarget();
            targetMethod = targetBean.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        } else {
            log.error("signature is not instanceof MethodSignature!");
        }
        //    3) 若无法解析 Method，则直接放行原方法，不再打印结构化日志
        if (targetMethod == null) {
            return pjp.proceed();
        }

        // 2. 执行原方法并组装结构化日志
        //    1) 记录开始时间、构造方法唯一标识（类名#方法名）
        long invokeStartMillis = System.currentTimeMillis();
        String methodKey = pjp.getTarget().getClass().getSimpleName() + "#" + pjp.getSignature().getName();
        
        //    2) 采样率判断：在执行原方法前判断是否需要打印日志
        boolean shouldLog = logConfiguration.shouldSample(methodKey);
        
        Object res = null;
        try {
            //    3) 执行被代理的原方法，捕获返回值
            res = pjp.proceed();
        } finally {

            //    4) 命中采样才执行日志打印逻辑
            if (shouldLog) {
                try {
                    //    5) 动态开关优先级最高：运行时配置可覆盖注解上的静态开关
                    Pair<Boolean, Boolean> dynamicLogSwitch = logConfiguration.dynamicLogRes(methodKey);
                    boolean enableReqLog = Optional.ofNullable(dynamicLogSwitch).map(Pair::getLeft).orElse(logIndex.enableReq());
                    boolean enableResLog = Optional.ofNullable(dynamicLogSwitch).map(Pair::getRight).orElse(logIndex.enableRes());

                    //    6) 若请求和响应都不打印，跳过
                    if (enableReqLog || enableResLog) {
                        Map<String, Object> logMap = new HashMap<>(8);

                        //    7) 入参和出参日志打印
                        if (enableReqLog) {
                            Parameter[] methodParameters = targetMethod.getParameters();
                            Object req = getRequestParams(methodArgs, methodParameters);
                            logMap.put("req", req);
                        }
                        if (enableResLog) {
                            logMap.put("res", res);
                        }

                        //    8) 慢调用判定：耗时超过注解阈值则标记 timeOut
                        long cost = System.currentTimeMillis() - invokeStartMillis;
                        if (logIndex.estimateTime() > -1 && cost >= logIndex.estimateTime()) {
                            logMap.put("timeOut", true);
                        }
                        logMap.put("cost", cost + "ms");
                        //    9) 统一格式输出：[业务标识], 类名.方法名 -> [JSON]
                        log.info(" [{} {}]-> {}", logIndex.value(), methodKey, JsonUtils.toString(logMap));
                    }
                } catch (Exception e) {
                    // 3. 异常兜底：仅打印警告，不影响主流程日志输出
                    log.warn("LogAspect, error", e);
                }
            }
        }

        // 3. 透传原方法的返回值，保持业务无感
        return res;
    }

    /**
     * 将切面捕获到的原始参数数组与方法参数元数据，重构为可读性更强的日志结构。
     * <p>
     * 规则：
     * <ul>
     *     <li>HttpServletRequest / HttpServletResponse 用占位字符串替换，避免巨型对象污染日志。</li>
     *     <li>单参数（且非 Servlet 对象）时直接返回该参数本体，简化日志结构。</li>
     *     <li>多参数时返回 List<Map>，每个 Map 保留 paramName -> paramValue 的对应关系。</li>
     * </ul>
     *
     * @param args       方法实际传入的参数数组
     * @param parameters 方法签名上的参数元数据（用于提取参数名）
     * @return 适合 JSON 序列化打印的请求参数结构；当 args 为 null 时返回 null
     */
    public Object getRequestParams(Object[] args, Parameter[] parameters) {
        // 1. 入参前置校验
        //    1) 参数数组为 null，直接返回，避免后续 NPE
        if (Objects.isNull(args)) {
            return null;
        }
        //    2) 单参数捷径：非 Servlet 类型时直接返回参数本体
        if (args.length == 1 && !(args[0] instanceof HttpServletRequest) && !(args[0] instanceof HttpServletResponse)) {
            return args[0];
        }

        // 2. 多参数场景：逐个参数转换为可读结构
        List<Object> requestParamList = new ArrayList<>();
        try {
            for (int paramIndex = 0; paramIndex < args.length; paramIndex++) {
                Object paramValue = args[paramIndex];
                //    1) Servlet 请求对象用占位符替换，避免序列化体积过大
                if (paramValue instanceof HttpServletRequest) {
                    requestParamList.add("HttpServletRequest");
                    continue;
                }
                //    2) Servlet 响应对象用占位符替换
                if (paramValue instanceof HttpServletResponse) {
                    requestParamList.add("HttpServletResponse");
                    continue;
                }
                //    3) 普通参数：包装为 paramName -> paramValue 的单键 Map
                Map<Object, Object> paramEntry = new HashMap<>(2);
                String paramName = (paramIndex < parameters.length) ? parameters[paramIndex].getName() : "arg" + paramIndex;
                paramEntry.put(paramName, paramValue);
                requestParamList.add(paramEntry);
            }
        } catch (Exception ex) {
            // 3. 异常兜底：仅打印警告，不影响主流程日志输出
            log.warn("LogAspect getRequestParams error", ex);
        }
        return requestParamList;
    }


}