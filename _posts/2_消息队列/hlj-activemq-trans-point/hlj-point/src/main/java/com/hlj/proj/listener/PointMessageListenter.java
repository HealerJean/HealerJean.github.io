package com.hlj.proj.listener;

import com.hlj.proj.api.point.PointEventService;
import com.hlj.proj.api.point.PointService;
import com.hlj.proj.constants.JmsConstant;
import com.hlj.proj.data.dao.mybatis.manager.point.TeventManager;
import com.hlj.proj.data.pojo.user.Tevent;
import com.hlj.proj.dto.Point.EventDTO;
import com.hlj.proj.enums.BusinessEnum;
import com.hlj.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;


/**
 * @author HealerJean
 * @ClassName PointMessageListenter
 * @date 2019/9/9  15:13.
 * @Description
 */
@Service
@Slf4j
public class PointMessageListenter {

    @Autowired
    private PointEventService pointEventService ;

    /**
     * 监听topic消息并保存到数据库中
     */
    @JmsListener(destination = JmsConstant.TOPIC_DISTRIBUTE_TRANSACTION, containerFactory = "jmsTopicListenerContainerFactory")
    public void listenTopic(String message) {
        log.info("接收到topic消息：" + message);
        EventDTO eventDTO = new EventDTO();
        eventDTO.setType(BusinessEnum.EventType.新增积分.code);
        eventDTO.setProcess(BusinessEnum.EventProcess.已发布.code);
        eventDTO.setContent(message);
        pointEventService.addEvent(eventDTO);
    }

}
