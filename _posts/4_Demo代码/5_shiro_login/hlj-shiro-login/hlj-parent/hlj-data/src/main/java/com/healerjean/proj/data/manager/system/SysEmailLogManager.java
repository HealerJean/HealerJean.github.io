/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.manager.system;

import java.util.List;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.system.SysEmailLogDao;
import com.healerjean.proj.data.pojo.system.SysEmailLog;
import com.healerjean.proj.data.pojo.system.SysEmailLogPage;
import com.healerjean.proj.data.pojo.system.SysEmailLogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author zhangyujin
 * @ClassName: SysEmailLogManager
 * @date 2099/1/1
 * @Description: SysEmailLogManager
 */
@Component("sysEmailLogManager")
public class SysEmailLogManager {

    @Autowired
    @Qualifier("sysEmailLogDao")
    private SysEmailLogDao sysEmailLogDao;

    public SysEmailLogDao getDao() {
        return this.sysEmailLogDao;
    }

    public int save(SysEmailLog sysEmailLog) {
        int cnt = 0;
        if (sysEmailLog.getId() == null) {
            cnt = sysEmailLogDao.insertSelective(sysEmailLog);
        } else {
            cnt = sysEmailLogDao.updateByPrimaryKeySelective(sysEmailLog);
        }
        return cnt;
    }

    public int update(SysEmailLog sysEmailLog) {
        return sysEmailLogDao.updateByPrimaryKey(sysEmailLog);
    }

    public int updateSelective(SysEmailLog sysEmailLog) {
        return sysEmailLogDao.updateByPrimaryKeySelective(sysEmailLog);
    }

    public int insert(SysEmailLog sysEmailLog) {
        return sysEmailLogDao.insert(sysEmailLog);
    }

    public int insertSelective(SysEmailLog sysEmailLog) {
        return sysEmailLogDao.insertSelective(sysEmailLog);
    }

    public int batchInsert(List<SysEmailLog> list) {
        return sysEmailLogDao.batchInsert(list);
    }

    public SysEmailLog findById(long id) {
        return sysEmailLogDao.selectByPrimaryKey(id);
    }

    public SysEmailLog findByQueryContion(SysEmailLogQuery query) {
        List<SysEmailLog> list = sysEmailLogDao.selectByExample(query);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public List<SysEmailLog> queryList(SysEmailLogQuery query) {
        return sysEmailLogDao.selectByExample(query);
    }

    public int deleteById(long id) {
        return sysEmailLogDao.deleteByPrimaryKey(id);
    }

    public int delete(SysEmailLogQuery query) {
        return sysEmailLogDao.deleteByExample(query);
    }

    public SysEmailLogPage queryPageList(SysEmailLogQuery query) {
        SysEmailLogPage sysEmailLogPage = new SysEmailLogPage();
        Integer itemCount = sysEmailLogDao.countByExample(query);
        query.setItemCount(itemCount);

        if (itemCount == 0) {
            sysEmailLogPage.setValues(null);
        } else {
            sysEmailLogPage.setValues(sysEmailLogDao.selectPageByExample(query));
        }

        sysEmailLogPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
        return sysEmailLogPage;
    }

}
