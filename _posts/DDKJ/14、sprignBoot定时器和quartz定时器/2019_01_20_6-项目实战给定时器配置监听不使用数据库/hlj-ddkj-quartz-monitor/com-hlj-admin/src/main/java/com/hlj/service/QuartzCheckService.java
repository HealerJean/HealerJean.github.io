package com.hlj.service;

import com.hlj.data.general.AppException;

import java.util.Calendar;

/**
 *
 * @version 1.0.0
 */
public interface QuartzCheckService {

    /**
     * 检查任务列表
     * @param calendar
     */
     void checkQuartzJob(Calendar calendar) throws AppException;
}
