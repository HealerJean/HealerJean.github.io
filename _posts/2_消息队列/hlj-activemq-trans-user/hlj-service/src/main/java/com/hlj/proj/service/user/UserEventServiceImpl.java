package com.hlj.proj.service.user;

import com.hlj.proj.api.user.UserEventService;
import com.hlj.proj.common.JmsConstant;
import com.hlj.proj.data.dao.mybatis.manager.user.TeventManager;
import com.hlj.proj.data.pojo.user.Tevent;
import com.hlj.proj.dto.user.EventDTO;
import com.hlj.proj.enums.BusinessEnum;
import com.hlj.proj.service.jms.JMSProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * @author HealerJean
 * @ClassName UserEventService
 * @date 2019/9/9  14:28.
 * @Description
 */
@Service
@Slf4j
public class UserEventServiceImpl implements UserEventService {

    @Autowired
    private TeventManager teventManager;

    @Autowired
    private JMSProducer jmsProducer;

    /**
     * 添加事件
     */
    @Override
    public void addEvent(EventDTO eventDTO) {
        Tevent tevent = new Tevent();
        tevent.setType(eventDTO.getType());
        tevent.setProcess(eventDTO.getProcess());
        tevent.setContent(eventDTO.getContent());
        teventManager.insertSelective(tevent);
    }

    /**
     * 执行事件
     * 1、activemq 发送topic消息
     * c
     */
    @Override
    public void executeAddUserEvent(EventDTO eventDTO) {
        // 1、activemq 发送topic消息
        String messageContent = eventDTO.getContent();
        Destination topic = new ActiveMQTopic(JmsConstant.TOPIC_DISTRIBUTE_TRANSACTION);
        jmsProducer.sendMessage(topic, messageContent);
        log.info("发送topic消息：{}", eventDTO);

        // 2、更新事件状态
        Tevent tevent = teventManager.findById(eventDTO.getEventId());
        tevent.setProcess(BusinessEnum.EventProcess.已发布.code);
        teventManager.updateSelective(tevent);
    }


}
