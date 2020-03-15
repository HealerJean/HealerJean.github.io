/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.manager.system;

import java.util.List;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.system.SysRoleMenuRefDao;
import com.healerjean.proj.data.pojo.system.SysRoleMenuRef;
import com.healerjean.proj.data.pojo.system.SysRoleMenuRefPage;
import com.healerjean.proj.data.pojo.system.SysRoleMenuRefQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author zhangyujin
 * @ClassName: SysRoleMenuRefManager
 * @date 2099/1/1
 * @Description: SysRoleMenuRefManager
 */
@Component("sysRoleMenuRefManager")
public class SysRoleMenuRefManager {

    @Autowired
    @Qualifier("sysRoleMenuRefDao")
    private SysRoleMenuRefDao sysRoleMenuRefDao;

    public SysRoleMenuRefDao getDao() {
        return this.sysRoleMenuRefDao;
    }

    public int save(SysRoleMenuRef sysRoleMenuRef) {
        int cnt = 0;
        if (sysRoleMenuRef.getId() == null) {
            cnt = sysRoleMenuRefDao.insertSelective(sysRoleMenuRef);
        } else {
            cnt = sysRoleMenuRefDao.updateByPrimaryKeySelective(sysRoleMenuRef);
        }
        return cnt;
    }

    public int update(SysRoleMenuRef sysRoleMenuRef) {
        return sysRoleMenuRefDao.updateByPrimaryKey(sysRoleMenuRef);
    }

    public int updateSelective(SysRoleMenuRef sysRoleMenuRef) {
        return sysRoleMenuRefDao.updateByPrimaryKeySelective(sysRoleMenuRef);
    }

    public int insert(SysRoleMenuRef sysRoleMenuRef) {
        return sysRoleMenuRefDao.insert(sysRoleMenuRef);
    }

    public int insertSelective(SysRoleMenuRef sysRoleMenuRef) {
        return sysRoleMenuRefDao.insertSelective(sysRoleMenuRef);
    }

    public int batchInsert(List<SysRoleMenuRef> list) {
        return sysRoleMenuRefDao.batchInsert(list);
    }

    public SysRoleMenuRef findById(long id) {
        return sysRoleMenuRefDao.selectByPrimaryKey(id);
    }

    public SysRoleMenuRef findByQueryContion(SysRoleMenuRefQuery query) {
        List<SysRoleMenuRef> list = sysRoleMenuRefDao.selectByExample(query);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public List<SysRoleMenuRef> queryList(SysRoleMenuRefQuery query) {
        return sysRoleMenuRefDao.selectByExample(query);
    }

    public int deleteById(long id) {
        return sysRoleMenuRefDao.deleteByPrimaryKey(id);
    }

    public int delete(SysRoleMenuRefQuery query) {
        return sysRoleMenuRefDao.deleteByExample(query);
    }

    public SysRoleMenuRefPage queryPageList(SysRoleMenuRefQuery query) {
        SysRoleMenuRefPage sysRoleMenuRefPage = new SysRoleMenuRefPage();
        Integer itemCount = sysRoleMenuRefDao.countByExample(query);
        query.setItemCount(itemCount);

        if (itemCount == 0) {
            sysRoleMenuRefPage.setValues(null);
        } else {
            sysRoleMenuRefPage.setValues(sysRoleMenuRefDao.selectPageByExample(query));
        }

        sysRoleMenuRefPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
        return sysRoleMenuRefPage;
    }

}
