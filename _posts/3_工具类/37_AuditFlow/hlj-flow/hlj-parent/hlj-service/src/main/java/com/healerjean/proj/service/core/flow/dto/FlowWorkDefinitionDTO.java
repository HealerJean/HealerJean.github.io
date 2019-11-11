package com.healerjean.proj.service.core.flow.dto;

import lombok.Data;

/**
 * @author HealerJean
 * @ClassName FlowWorkDefinitionDTO
 * @date 2019/11/11  16:40.
 * @Description
 */
@Data
public class FlowWorkDefinitionDTO {

    /** 工作流编码 */
    private String flowCode;
    /** 工作流名称 */
    private String flowName;
    /** 节点顺序流程 */
    private String flowDefinition;

}

