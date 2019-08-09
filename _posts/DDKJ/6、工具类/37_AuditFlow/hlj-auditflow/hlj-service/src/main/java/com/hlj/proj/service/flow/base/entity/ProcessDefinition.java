package com.hlj.proj.service.flow.base.entity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hlj.proj.data.dao.mybatis.manager.flow.ScfFlowDefinitionManager;
import com.hlj.proj.data.dao.mybatis.manager.flow.ScfFlowNodeManager;
import com.hlj.proj.data.dao.mybatis.manager.flow.ScfFlowRecordManager;
import com.hlj.proj.data.pojo.flow.*;
import com.hlj.proj.exception.BusinessException;
import com.hlj.proj.service.flow.service.enums.FlowNodeTypeEnum;
import com.hlj.proj.service.spring.SpringContextHolder;
import com.hlj.proj.utils.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName ProcessDefinition
 * @Author TD
 * @Date 2019/6/12 9:51
 * @Description 流程定义
 */
@Data
@Slf4j
public class ProcessDefinition {

    /**
     * 通过流程编号获取实例
     */
    public static ProcessDefinition of(String flowCode) {
        ScfFlowDefinitionManager scfFlowDefinitionManager = SpringContextHolder.getBean(ScfFlowDefinitionManager.class);
        ScfFlowDefinitionQuery scfFlowDefinitionQuery = new ScfFlowDefinitionQuery();
        scfFlowDefinitionQuery.setFlowCode(flowCode);
        //获取流程定义
        ScfFlowDefinition scfFlowDefinition = scfFlowDefinitionManager.findByQueryContion(scfFlowDefinitionQuery);
        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setFlowName(scfFlowDefinition.getFlowName());
        processDefinition.setFlowCode(scfFlowDefinition.getFlowCode());
        //整理流程节点序列
        String flowDefinitionString = scfFlowDefinition.getFlowDefinition();
        List<FlowNode> list = getNodeList(null , flowDefinitionString);
        processDefinition.setNodes(list);
        return processDefinition;
    }

    /**
     * 通过流程编号获取实例
     */
    public static Process ofInstant (String instantsNo){
        if (StringUtils.isBlank(instantsNo)) {
            throw new BusinessException("找不到对应的流程(" + instantsNo + ")");
        }
        ScfFlowRecordManager scfFlowRecordManager = SpringContextHolder.getBean(ScfFlowRecordManager.class);
        ScfFlowDefinitionManager scfFlowDefinitionManager = SpringContextHolder.getBean(ScfFlowDefinitionManager.class);
        ScfFlowRecordQuery scfFlowRecordQuery = new ScfFlowRecordQuery();
        scfFlowRecordQuery.setInstantsNo(instantsNo);
        scfFlowRecordQuery.setStatus(Result.StatusEnum.Suspend.getCode());
        ScfFlowRecord scfFlowRecordOld = scfFlowRecordManager.findByQueryContion(scfFlowRecordQuery);
        if(scfFlowRecordOld == null){
            throw new BusinessException("找不到对应的流程(" + instantsNo + ")");
        }
        String flowCode = scfFlowRecordOld.getFlowCode();
        Process process = new Process();
        process.setFlowName(scfFlowRecordOld.getFlowName());
        process.setFlowCode(flowCode);
        process.setSept(new AtomicInteger(scfFlowRecordOld.getSept()));
        process.setInstantsNo(instantsNo);
        ScfFlowDefinitionQuery scfFlowDefinitionQuery = new ScfFlowDefinitionQuery();
        scfFlowDefinitionQuery.setFlowCode(flowCode);
        //获取流程定义
        ScfFlowDefinition scfFlowDefinition = scfFlowDefinitionManager.findByQueryContion(scfFlowDefinitionQuery);
        String flowDefinitionString = scfFlowDefinition.getFlowDefinition();
        List<FlowNode> nodes = getNodeList( instantsNo , flowDefinitionString);
        process.setNodes( nodes);
        return process;


    }

    private static List<FlowNode> getNodeList(String instantsNo , String flowDefinitionString) {
        if (StringUtils.isBlank(flowDefinitionString)) {
            return null;
        }
        List<String> linkedList = null;
                ScfFlowNodeManager scfFlowNodeManager = SpringContextHolder.getBean(ScfFlowNodeManager.class);
        try {
            linkedList = JsonUtils.toObject(flowDefinitionString, new TypeReference<LinkedList<String>>() { });
        }catch (Exception e){
            log.error("流程信息配置错误" , e);
            throw new BusinessException("流程信息配置错误");
        }
        List<FlowNode> list = new LinkedList<>();
        for (int i = 0; i < linkedList.size(); i++) {
            String nodeCode = linkedList.get(i);
            ScfFlowNodeQuery scfFlowNodeQuery = new ScfFlowNodeQuery();
            scfFlowNodeQuery.setNodeCode(nodeCode);
            //获取流程节点
            ScfFlowNode scfFlowNode = scfFlowNodeManager.findByQueryContion(scfFlowNodeQuery);
            if(scfFlowNode == null){
                throw new BusinessException("找不到对应的流程节点(" + nodeCode + ")");
            }
            String nodeType = scfFlowNode.getNodeType();
            FlowNode flowNode = null;
            switch(FlowNodeTypeEnum.toEnum(nodeType)){
                case ServiceNode:
                    try {
                        flowNode = SpringContextHolder.getBean(nodeCode, FlowNode.class);
                    } catch (NoSuchBeanDefinitionException e) {
                        log.error("找不到对应的流程节点:{}", nodeCode);
                        throw new BusinessException("找不到对应的流程节点(" + nodeCode + ")");
                    }
                    break;
                case AuditNode:
                    flowNode = AuditorFlowNode.of(scfFlowNode.getNodeCode(),scfFlowNode.getNodeName(),
                            scfFlowNode.getNodeType(),scfFlowNode.getNodeDetail(), instantsNo, i);
                    break;
                default:
                    break;
            }
            list.add(flowNode);
        }
        return list;
    }

    /**
     * 流程编号
     */
    private String flowCode;
    /**
     * 流程名称
     */
    private String flowName;
    /**
     * 流程节点
     */
    private List<FlowNode> nodes;
    /**
     * 流程节点编号
     */
    private List<String> nodeCodes;

    /**
     * 创建一个流程
     */
    public Process newInstants() {
        //创建一个流程得整个实例到数据库中
        //创建对应的审批结点实例
        Process process = new Process();
        process.setFlowName(flowName);
        process.setFlowCode(flowCode);
        process.setSept(new AtomicInteger(1));
        String instantsNo = UUID.randomUUID().toString().replace("-","");
        process.setInstantsNo(instantsNo);
        process.setNodes(nodes);
        return process;
    }
}
