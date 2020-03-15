/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.manager.system;

import java.util.List;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.system.SysUserRoleRefDao;
import com.healerjean.proj.data.pojo.system.SysRole;
import com.healerjean.proj.data.pojo.system.SysUserRoleRef;
import com.healerjean.proj.data.pojo.system.SysUserRoleRefPage;
import com.healerjean.proj.data.pojo.system.SysUserRoleRefQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author zhangyujin
 * @ClassName: SysUserRoleRefManager
 * @date 2099/1/1
 * @Description: SysUserRoleRefManager
 */
@Component("sysUserRoleRefManager")
public class SysUserRoleRefManager {

    @Autowired
    @Qualifier("sysUserRoleRefDao")
    private SysUserRoleRefDao sysUserRoleRefDao;

    public SysUserRoleRefDao getDao() {
        return this.sysUserRoleRefDao;
    }

    public int save(SysUserRoleRef sysUserRoleRef) {
        int cnt = 0;
        if (sysUserRoleRef.getId() == null) {
            cnt = sysUserRoleRefDao.insertSelective(sysUserRoleRef);
        } else {
            cnt = sysUserRoleRefDao.updateByPrimaryKeySelective(sysUserRoleRef);
        }
        return cnt;
    }

    public int update(SysUserRoleRef sysUserRoleRef) {
        return sysUserRoleRefDao.updateByPrimaryKey(sysUserRoleRef);
    }

    public int updateSelective(SysUserRoleRef sysUserRoleRef) {
        return sysUserRoleRefDao.updateByPrimaryKeySelective(sysUserRoleRef);
    }

    public int insert(SysUserRoleRef sysUserRoleRef) {
        return sysUserRoleRefDao.insert(sysUserRoleRef);
    }

    public int insertSelective(SysUserRoleRef sysUserRoleRef) {
        return sysUserRoleRefDao.insertSelective(sysUserRoleRef);
    }

    public int batchInsert(List<SysUserRoleRef> list) {
        return sysUserRoleRefDao.batchInsert(list);
    }

    public SysUserRoleRef findById(long id) {
        return sysUserRoleRefDao.selectByPrimaryKey(id);
    }

    public SysUserRoleRef findByQueryContion(SysUserRoleRefQuery query) {
        List<SysUserRoleRef> list = sysUserRoleRefDao.selectByExample(query);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public List<SysUserRoleRef> queryList(SysUserRoleRefQuery query) {
        return sysUserRoleRefDao.selectByExample(query);
    }

    public int deleteById(long id) {
        return sysUserRoleRefDao.deleteByPrimaryKey(id);
    }

    public int delete(SysUserRoleRefQuery query) {
        return sysUserRoleRefDao.deleteByExample(query);
    }

    public SysUserRoleRefPage queryPageList(SysUserRoleRefQuery query) {
        SysUserRoleRefPage sysUserRoleRefPage = new SysUserRoleRefPage();
        Integer itemCount = sysUserRoleRefDao.countByExample(query);
        query.setItemCount(itemCount);

        if (itemCount == 0) {
            sysUserRoleRefPage.setValues(null);
        } else {
            sysUserRoleRefPage.setValues(sysUserRoleRefDao.selectPageByExample(query));
        }

        sysUserRoleRefPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
        return sysUserRoleRefPage;
    }


    public List<SysRole> queryListToRole(SysUserRoleRefQuery query) {
        return sysUserRoleRefDao.queryListToRole(query);
    }

}
