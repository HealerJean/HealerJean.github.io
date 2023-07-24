package com.healerjean.proj.liteflow.flow.normal;

import com.alibaba.fastjson.JSON;
import com.healerjean.proj.liteflow.context.DemoContext;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * BCmp
 *
 * @author zhangyujin
 * @date 2023 /7/24
 */
@Slf4j
@LiteflowComponent("流程B")
public class BCmp extends NodeComponent {

    /**
     * 表示是否进入该节点，可以用于业务参数的预先判断
     *
     * @return boolean
     */
    @Override
    public boolean isAccess() {
        return true;
    }

    /**
     * 流程前置处理器 isAccess 之后执行。
     */
    @Override
    public void beforeProcess() {
    }


    /**
     * 流程执行
     */
    @Override
    public void process() {
        log.info("[BCmp#]process");

        // 1、获取组件ID。
        String nodeId = this.getNodeId();
        log.info("[BCmp#process] this.getNodeId():{}", nodeId);

        // 2、获取组件别名。
        String name = this.getName();
        log.info("[BCmp#process] this.getName():{}", name);

        // 3、获取当前执行的流程Id。
        String chainId = this.getChainId();
        log.info("[BCmp#process] this.getChainId():{}", chainId);

        // 4、获取流程的初始参数。。
        DemoContext requestData = this.getRequestData();
        log.info("[BCmp#process] this.getRequestData():{}", JSON.toJSONString(requestData));

        // 5、表示是否立即结束整个流程 ，用法为this.setIsEnd(true)。对于这种方式，由于是用户主动结束的流程，属于正常结束，所以最终的isSuccess是为true的。
        // 需要注意的是，如果isContinueOnError为true的情况下，调用了this.setIsEnd(true)，那么依旧会终止。response里的isSuccess还是true
        this.setIsEnd(false);

        // 6、获取这个组件的标签信息，关于标签的定义和使用，请参照组件标签。
        String tag = this.getTag();
        log.info("[BCmp#process] this.getTag():{}", tag);

    }


    /**
     * 如果覆盖后，返回true，则表示在这个组件执行完之后立马终止整个流程。对于这种方式，由于是用户主动结束的流程，属于正常结束，所以最终的isSuccess是为true的。
     */
    @Override
    public boolean isEnd() {
        return false;
    }


    /**
     * 流程后置处理器
     */
    @Override
    public void afterProcess() {
    }


    /**
     * 流程的成功事件回调
     */
    @Override
    public void onSuccess() {
    }


    /**
     * 流程的失败事件回调
     * 1、onError方法执行后，因为主方法抛出异常，所以整个流程依旧是失败状态。response对象里依旧是主方法抛出的错。
     * 2、如果onError方法本身抛错，那么最终抛到最外面的错，是主方法里的错，而onError方法所产生的异常会被打出堆栈
     */
    @Override
    public void onError() {
    }


    /**
     * 表示出错是否继续往下执行下一个组件，默认为false
     */
    @Override
    public boolean isContinueOnError() {
        return false;
    }






}