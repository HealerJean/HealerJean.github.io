package com.healerjean.proj.service.core.flow.node;

import com.healerjean.proj.data.manager.flow.FlowWorkDefaultDefinitionManager;
import com.healerjean.proj.data.manager.flow.FlowWorkDefinitionManager;
import com.healerjean.proj.data.manager.flow.FlowWorkNodeManager;
import com.healerjean.proj.data.manager.flow.FlowWorkRecordManager;
import com.healerjean.proj.data.pojo.flow.*;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.enums.StatusEnum;
import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.service.core.flow.dto.FlowWorkDefinitionDTO;
import com.healerjean.proj.service.core.flow.dto.NodeTransferDataDTO;
import com.healerjean.proj.service.core.flow.enums.FlowEnum;
import com.healerjean.proj.utils.EmptyUtils;
import com.healerjean.proj.utils.InstansNoUtils;
import com.healerjean.proj.utils.JsonUtils;
import com.healerjean.proj.utils.SpringHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName ProcessDefinition
 * @Author TD
 * @Date 2019/6/12 9:51
 * @Description 流程定义
 */
@Slf4j
@Data
public class FlowWorkNodeProcess {

    /**
     * 根据默认配置初始化流定义
     *  1、根据默认的工作流配置生成我们要执行的工作流
     *  2、保存初始化流定义
     *  3、获取第一次开始执行的节点
     *  4、初始化所有的流程节点配置
     */
    public static FlowWorkNodeService instansWorkNode(String flowCode, LoginUserDTO loginUserDTO) {

        // 根据默认的工作流配置生成我们要执行的工作流
        FlowWorkDefaultDefinitionManager flowWorkDefaultDefinitionManager = SpringHelper.getBean(FlowWorkDefaultDefinitionManager.class);
        FlowWorkDefaultDefinitionQuery flowWorkDefinitionQuery = new FlowWorkDefaultDefinitionQuery();
        flowWorkDefinitionQuery.setFlowCode(flowCode);
        flowWorkDefinitionQuery.setStatus(StatusEnum.生效.code);
        FlowWorkDefaultDefinition defaultDefinition = flowWorkDefaultDefinitionManager.findByQueryContion(flowWorkDefinitionQuery);
        List<String> processDefinition = JsonUtils.jsonToArray(defaultDefinition.getFlowDefinition(), String.class);
        if (EmptyUtils.isEmpty(processDefinition)) {
            throw new BusinessException("工作流配置不能为空");
        }

        // 2、保存初始化流定义
        String instantsNo = InstansNoUtils.uniqueSeq();
        FlowWorkDefinition flowWorkDefinition = new FlowWorkDefinition();
        flowWorkDefinition.setInstantsNo(instantsNo);
        flowWorkDefinition.setFlowCode(defaultDefinition.getFlowCode());
        flowWorkDefinition.setFlowName(defaultDefinition.getFlowName());
        flowWorkDefinition.setFlowDefinition(defaultDefinition.getFlowDefinition());
        flowWorkDefinition.setStatus(StatusEnum.生效.code);
        flowWorkDefinition.setCreateUser(loginUserDTO.getUserId());
        flowWorkDefinition.setCreateName(loginUserDTO.getRealName());
        FlowWorkDefinitionManager flowWorkDefinitionManager = SpringHelper.getBean(FlowWorkDefinitionManager.class);
        flowWorkDefinitionManager.insertSelective(flowWorkDefinition);

        //3、获取第一次开始执行的节点
        String firstNode = processDefinition.get(0);
        FlowWorkNodeService flowWorkNodeService = SpringHelper.getBean(firstNode, FlowWorkNodeService.class);
        flowWorkNodeService.setFlowName(flowWorkDefinition.getFlowName());
        flowWorkNodeService.setFlowCode(flowWorkDefinition.getFlowCode());
        flowWorkNodeService.setInstantsNo(InstansNoUtils.uniqueSeq());

        // 4、初始化所有的流程节点配置
        AtomicReference<Integer> stepAtomic = new AtomicReference<>(1);
        processDefinition.stream().forEach(nodeCode -> {
            FlowWorkNodeManager flowWorkNodeManager = SpringHelper.getBean(FlowWorkNodeManager.class);
            FlowWorkNodeQuery flowWorkNodeQuery = new FlowWorkNodeQuery();
            flowWorkNodeQuery.setNodeCode(nodeCode);
            flowWorkNodeQuery.setStatus(StatusEnum.生效.code);
            FlowWorkNode flowWorkNode = flowWorkNodeManager.findByQueryContion(flowWorkNodeQuery);
            FlowWorkRecord workRecord = new FlowWorkRecord();
            Integer step = stepAtomic.get();
            if (step.intValue() == 1) {
                workRecord.setStatus(FlowEnum.WorkStatusEnum.执行中.code);
                flowWorkNodeService.setNodeCode(flowWorkNode.getNodeCode());
                flowWorkNodeService.setNodeName(flowWorkNode.getNodeName());
                flowWorkNodeService.setStatus(FlowEnum.WorkStatusEnum.执行中.code);
                flowWorkNodeService.setFlowNodeTypeEnum(FlowEnum.FlowNodeTypeEnum.toEnum(flowWorkNode.getNodeType()));
            } else {
                workRecord.setStatus(FlowEnum.WorkStatusEnum.等待执行.code);
            }
            workRecord.setInstantsNo(instantsNo);
            workRecord.setFlowCode(flowWorkDefinition.getFlowCode());
            workRecord.setFlowName(flowWorkDefinition.getFlowName());
            workRecord.setStep(step);
            workRecord.setNodeCode(flowWorkNode.getNodeCode());
            workRecord.setNodeName(flowWorkNode.getNodeName());
            workRecord.setCreateUser(loginUserDTO.getUserId());
            workRecord.setCreateName(loginUserDTO.getRealName());
            workRecord.setNodeType(flowWorkNode.getNodeType());
            FlowWorkRecordManager flowWorkRecordManager = SpringHelper.getBean(FlowWorkRecordManager.class);
            flowWorkRecordManager.insertSelective(workRecord);
        });
        return flowWorkNodeService;
    }


    /**
     * 通过流程编号获取当前执行的工作流节点
     */
    public static FlowWorkNodeService currentWorkFlow(String instantsNo) {
        FlowWorkRecordManager flowWorkRecordManager = SpringHelper.getBean(FlowWorkRecordManager.class);
        FlowWorkRecordQuery flowWorkRecordQuery = new FlowWorkRecordQuery();
        flowWorkRecordQuery.setStatus(FlowEnum.WorkStatusEnum.执行中.code);
        flowWorkRecordQuery.setInstantsNo(instantsNo);
        FlowWorkRecord flowWorkRecord = flowWorkRecordManager.findByQueryContion(flowWorkRecordQuery);
        if (flowWorkRecord == null ){
            log.info("工作流对应的instansNo：【{}】没有执行中的工作流节点", instantsNo);
            throw new BusinessException("工作流对应的instansNo：【{"+instantsNo+"}】没有执行中的工作流节点");
        }
        FlowWorkNodeService flowWorkNodeService = SpringHelper.getBean(flowWorkRecord.getNodeCode(), FlowWorkNodeService.class);
        flowWorkNodeService.setInstantsNo(flowWorkRecord.getInstantsNo());
        flowWorkNodeService.setFlowCode(flowWorkRecord.getFlowCode());
        flowWorkNodeService.setFlowName(flowWorkRecord.getFlowName());
        flowWorkNodeService.setNodeCode(flowWorkRecord.getNodeCode());
        flowWorkNodeService.setNodeName(flowWorkRecord.getNodeName());
        flowWorkNodeService.setStatus(FlowEnum.WorkStatusEnum.执行中.code);
        flowWorkNodeService.setFlowNodeTypeEnum(FlowEnum.FlowNodeTypeEnum.toEnum(flowWorkRecord.getNodeType()));
        return flowWorkNodeService;
    }



    /**
     * 执行成功 直接进行下一个节点
     * 1、 更新当前节点的记录
     * 2、下个节点开始执行任务
     */
    public static FlowWorkNodeService successForNextWorkNode(String instantsNo, LoginUserDTO loginUserDTO) {
        FlowWorkRecordManager flowWorkRecordManager = SpringHelper.getBean(FlowWorkRecordManager.class);
        FlowWorkRecordQuery flowWorkRecordQuery = new FlowWorkRecordQuery();
        flowWorkRecordQuery.setStatus(FlowEnum.WorkStatusEnum.执行中.code);
        flowWorkRecordQuery.setInstantsNo(instantsNo);
        FlowWorkRecord flowWorkRecord = flowWorkRecordManager.findByQueryContion(flowWorkRecordQuery);
        if (flowWorkRecord == null ){
            log.info("工作流对应的instansNo：【{}】没有执行中的工作流节点", instantsNo);
            throw new BusinessException("工作流对应的instansNo：【{"+instantsNo+"}】没有执行中的工作流节点");
        }
        flowWorkRecord.setStatus(FlowEnum.WorkStatusEnum.成功.code);
        flowWorkRecord.setCreateUser(loginUserDTO.getUserId());
        flowWorkRecord.setCreateName(loginUserDTO.getRealName());
        flowWorkRecordManager.updateSelective(flowWorkRecord);
        log.info("工作流：【{}】--------实例编号：【{}】--------节点：【{}】--------第【{}】步--------执行成功", flowWorkRecord.getFlowName(), instantsNo, flowWorkRecord.getFlowName(), flowWorkRecord.getStep());

        flowWorkRecordQuery.setInstantsNo(instantsNo);
        flowWorkRecordQuery.setStep(flowWorkRecord.getStep()+1);
        FlowWorkRecord nextWorkRecord = flowWorkRecordManager.findByQueryContion(flowWorkRecordQuery);
        FlowWorkNodeService flowWorkNodeService = SpringHelper.getBean(nextWorkRecord.getNodeCode(), FlowWorkNodeService.class);
        flowWorkNodeService.setInstantsNo(nextWorkRecord.getInstantsNo());
        flowWorkNodeService.setFlowCode(nextWorkRecord.getFlowCode());
        flowWorkNodeService.setFlowName(nextWorkRecord.getFlowName());
        flowWorkNodeService.setNodeCode(nextWorkRecord.getNodeCode());
        flowWorkNodeService.setNodeName(nextWorkRecord.getNodeName());
        flowWorkNodeService.setStatus(FlowEnum.WorkStatusEnum.执行中.code);
        flowWorkNodeService.setFlowNodeTypeEnum(FlowEnum.FlowNodeTypeEnum.toEnum(nextWorkRecord.getNodeType()));

        //开启下一个节点
        flowWorkNodeService.dealBusiness(NodeTransferDataDTO.instanct(flowWorkNodeService.getFlowCode()), loginUserDTO);
        return flowWorkNodeService;
    }






}
