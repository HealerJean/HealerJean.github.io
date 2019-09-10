package com.hlj.proj.service.point;

import com.hlj.proj.api.point.PointEventService;
import com.hlj.proj.api.point.PointService;
import com.hlj.proj.data.dao.mybatis.manager.point.TeventManager;
import com.hlj.proj.data.pojo.user.Tevent;
import com.hlj.proj.data.pojo.user.Tpoint;
import com.hlj.proj.dto.Point.EventDTO;
import com.hlj.proj.dto.Point.PointDTO;
import com.hlj.proj.enums.BusinessEnum;
import com.hlj.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HealerJean
 * @ClassName PointEventServiceImpl
 * @date 2019/9/9  15:24.
 * @Description
 */
@Service
@Slf4j
public class PointEventServiceImpl implements PointEventService {

    @Autowired
    private TeventManager teventManager;
    @Autowired
    private PointService  pointService ;


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
     * 1、添加积分记录
     * 2、修改事件状态
     */
    @Override
    public void executeAddPointEvent(EventDTO eventDTO) {
        PointDTO  pointDTO = JsonUtils.jsonToObject(eventDTO.getContent(),PointDTO.class);
        pointService.addPoint(pointDTO);

        Tevent tevent = teventManager.findById(eventDTO.getEventId());
        tevent.setProcess(BusinessEnum.EventProcess.已处理.code);
        teventManager.updateSelective(tevent);
    }

}
