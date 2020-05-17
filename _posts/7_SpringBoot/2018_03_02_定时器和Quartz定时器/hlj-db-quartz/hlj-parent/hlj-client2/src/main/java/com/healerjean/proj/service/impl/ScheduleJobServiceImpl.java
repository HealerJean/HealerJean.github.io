package com.healerjean.proj.service.impl;

import com.healerjean.proj.dao.mapper.ScheduleJobMapper;
import com.healerjean.proj.service.ScheduleJobService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author HealerJean
 * @Description
 */
@Service("scheduleJobService")
public class ScheduleJobServiceImpl implements ScheduleJobService {

    @Autowired
    private ScheduleJobMapper scheduleJobMapper;

}
