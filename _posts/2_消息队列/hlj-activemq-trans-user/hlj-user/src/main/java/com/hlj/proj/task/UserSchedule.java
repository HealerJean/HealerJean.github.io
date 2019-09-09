package com.hlj.proj.task;

import com.hlj.proj.api.user.UserEventService;
import com.hlj.proj.data.dao.mybatis.manager.user.TeventManager;
import com.hlj.proj.data.pojo.user.Tevent;
import com.hlj.proj.data.pojo.user.TeventQuery;
import com.hlj.proj.dto.user.EventDTO;
import com.hlj.proj.enums.BusinessEnum;
import com.hlj.proj.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HealerJean
 * @ClassName UserScheduled
 * @date 2019/9/9  14:37.
 * @Description
 */
@Component
@Slf4j
public class UserSchedule {

    @Autowired
    private UserEventService userEventService;

    @Autowired
    private TeventManager teventManager;

    @Scheduled(cron = "0 */1 * * * ?")
    public void executeEvent() {
        log.info("定时器执行--------新增用户事件处理");
        TeventQuery teventQuery = new TeventQuery();
        teventQuery.setType(BusinessEnum.EventType.新增用户.code);
        teventQuery.setProcess(BusinessEnum.EventProcess.新建.code);
        List<Tevent> tevents = teventManager.queryList(teventQuery);
        if (!EmptyUtil.isEmpty(tevents)) {
            System.out.println("定时器执行--------新增的用户记录总数为" + tevents.size());
            List<EventDTO> eventDTOS = tevents.stream().map(item -> {
                EventDTO eventDTO = new EventDTO();
                eventDTO.setEventId(item.getId());
                eventDTO.setType(item.getType());
                eventDTO.setProcess(item.getProcess());
                eventDTO.setContent(item.getContent());
                return eventDTO;
            }).collect(Collectors.toList());

            for(EventDTO eventDTO : eventDTOS){
                try {
                    userEventService.executeAddUserEvent(eventDTO);
                }catch (Exception e){
                    log.error("定时器执行-----处理事件为{}发送异常,异常信息",e);
                    continue;
                }
                log.info("定时器执行-处理事件：{}处理成功", eventDTO);
            }

        } else {
            log.info("没有新增的用户记录，无需处理");
        }

    }

}
