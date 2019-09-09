package com.hlj.proj.dto.Point;

import lombok.Data;

/**
 * @author HealerJean
 * @ClassName EventDTO
 * @date 2019/9/9  14:24.
 * @Description
 */
@Data
public class EventDTO {

    /**
     * 事件Id
     */
    private Long eventId;

    /**
     * 类型
     */
    private String type;

    /**
     * 事件处理过程
     */
    private String process;

    /**
     * 事件的内容，用于保存事件发生时需要传递的数据
     */
    private String content;

}
