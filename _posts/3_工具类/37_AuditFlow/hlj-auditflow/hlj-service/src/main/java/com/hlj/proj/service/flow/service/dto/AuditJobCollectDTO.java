package com.hlj.proj.service.flow.service.dto;

import lombok.Data;


@Data
public class AuditJobCollectDTO {

    /**
     * 节点编号
     */
    private String nodeCode;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 数量
     */
    private Integer count;
}
