package com.hlj.proj.task;

import com.hlj.proj.api.point.PointEventService;
import com.hlj.proj.data.dao.mybatis.manager.point.TeventManager;
import com.hlj.proj.data.pojo.user.Tevent;
import com.hlj.proj.data.pojo.user.TeventQuery;
import com.hlj.proj.dto.Point.EventDTO;
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
 * @ClassName PointSchedule
 * @date 2019/9/9  15:33.
 * @Description
 */
@Component
@Slf4j
public class PointSchedule {

    @Autowired
    private TeventManager teventManager ;

    @Autowired
    private PointEventService pointEventService ;

    @Scheduled(cron = "0 */1 * * * ?")
    public void executeEvent(){
        log.info("定时器执行--------新增积分事件处理");
        TeventQuery teventQuery = new TeventQuery();
        teventQuery.setType(BusinessEnum.EventType.新增积分.code);
        teventQuery.setProcess(BusinessEnum.EventProcess.已发布.code);
        List<Tevent> tevents = teventManager.queryList(teventQuery);
        if (!EmptyUtil.isEmpty(tevents)) {
            System.out.println("定时器执行--------新增积分记录总数为" + tevents.size());
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
                    pointEventService.executeAddPointEvent(eventDTO);
                }catch (Exception e){
                    log.info("定时器执行-----处理事件为{}发送异常",eventDTO);
                    continue;
                }
                log.info("定时器执行-处理事件：{}处理成功", eventDTO);
            }
        } else {
            log.info("没有新增的用户记录，无需处理");
        }
    }

}
