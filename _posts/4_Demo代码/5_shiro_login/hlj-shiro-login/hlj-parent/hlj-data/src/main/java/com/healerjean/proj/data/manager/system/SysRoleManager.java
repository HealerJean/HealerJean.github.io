/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.manager.system;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.system.SysRoleDao;
import com.healerjean.proj.data.pojo.system.SysRole;
import com.healerjean.proj.data.pojo.system.SysRolePage;
import com.healerjean.proj.data.pojo.system.SysRoleQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: SysRoleManager
 * @date 2099/1/1
 * @Description: SysRoleManager
 */
@Component("sysRoleManager")
public class SysRoleManager {

    @Autowired
    @Qualifier("sysRoleDao")
    private SysRoleDao sysRoleDao;

    public SysRoleDao getDao() {
        return this.sysRoleDao;
    }

    public int save(SysRole sysRole) {
        int cnt = 0;
        if (sysRole.getId() == null) {
            cnt = sysRoleDao.insertSelective(sysRole);
        } else {
            cnt = sysRoleDao.updateByPrimaryKeySelective(sysRole);
        }
        return cnt;
    }

    public int update(SysRole sysRole) {
        return sysRoleDao.updateByPrimaryKey(sysRole);
    }

    public int updateSelective(SysRole sysRole) {
        return sysRoleDao.updateByPrimaryKeySelective(sysRole);
    }

    public int insert(SysRole sysRole) {
        return sysRoleDao.insert(sysRole);
    }

    public int insertSelective(SysRole sysRole) {
        return sysRoleDao.insertSelective(sysRole);
    }

    public int batchInsert(List<SysRole> list) {
        return sysRoleDao.batchInsert(list);
    }

    public SysRole findById(long id) {
        return sysRoleDao.selectByPrimaryKey(id);
    }

    public SysRole findByQueryContion(SysRoleQuery query) {
        List<SysRole> list = sysRoleDao.selectByExample(query);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public List<SysRole> queryList(SysRoleQuery query) {
        return sysRoleDao.selectByExample(query);
    }

    public int deleteById(long id) {
        return sysRoleDao.deleteByPrimaryKey(id);
    }

    public int delete(SysRoleQuery query) {
        return sysRoleDao.deleteByExample(query);
    }

    public SysRolePage queryPageList(SysRoleQuery query) {
        SysRolePage sysRolePage = new SysRolePage();
        Integer itemCount = sysRoleDao.countByExample(query);
        query.setItemCount(itemCount);

        if (itemCount == 0) {
            sysRolePage.setValues(null);
        } else {
            sysRolePage.setValues(sysRoleDao.selectPageByExample(query));
        }

        sysRolePage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
        return sysRolePage;
    }


    public List<SysRole> queryListLike(SysRoleQuery query) {
        return sysRoleDao.selectByExampleLike(query);
    }


    public SysRolePage queryPageListLike(SysRoleQuery query) {
        SysRolePage rolePage = new SysRolePage();
        Integer itemCount = sysRoleDao.countByExampleLike(query);
        query.setItemCount(itemCount);
        if (itemCount == 0) {
            rolePage.setValues(null);
        } else {
            rolePage.setValues(sysRoleDao.selectPageByExampleLike(query));
        }
        rolePage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
        return rolePage;
    }

}
