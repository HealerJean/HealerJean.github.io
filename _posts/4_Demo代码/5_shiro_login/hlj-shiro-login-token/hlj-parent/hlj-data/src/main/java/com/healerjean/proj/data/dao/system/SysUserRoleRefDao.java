/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.dao.system;

import java.util.List;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.system.SysRole;
import com.healerjean.proj.data.pojo.system.SysUserRoleRef;
import com.healerjean.proj.data.pojo.system.SysUserRoleRefQuery;
import org.springframework.stereotype.Repository;

/**
 * @author zhangyujin
 * @ClassName: SysUserRoleRefDao
 * @date 2099/1/1
 * @Description: SysUserRoleRefDao
 */
@Repository("sysUserRoleRefDao")
public class SysUserRoleRefDao extends BaseDao {

    public int countByExample(SysUserRoleRefQuery example) {
        return super.getSqlSession().selectOne("SysUserRoleRefMapper.countByExample", example);
    }

    public int deleteByExample(SysUserRoleRefQuery example) {
        return super.getSqlSession().delete("SysUserRoleRefMapper.deleteByExample", example);
    }

    public int deleteByPrimaryKey(long id) {
        return super.getSqlSession().delete("SysUserRoleRefMapper.deleteByPrimaryKey", id);
    }

    public int insert(SysUserRoleRef record) {
        return super.getSqlSession().insert("SysUserRoleRefMapper.insert", record);
    }

    public int insertSelective(SysUserRoleRef record) {
        return super.getSqlSession().insert("SysUserRoleRefMapper.insertSelective", record);
    }

    public int batchInsert(List<SysUserRoleRef> list) {
        return super.batchInsert("SysUserRoleRefMapper.insertSelective", list);
    }

    public List<SysUserRoleRef> selectByExample(SysUserRoleRefQuery example) {
        return super.getSqlSession().selectList("SysUserRoleRefMapper.selectByExample", example);
    }

    public List<SysUserRoleRef> selectPageByExample(SysUserRoleRefQuery example) {
        return super.getSqlSession().selectList("SysUserRoleRefMapper.selectPageByExample", example);
    }

    public SysUserRoleRef selectByPrimaryKey(long id) {
        return super.getSqlSession().selectOne("SysUserRoleRefMapper.selectByPrimaryKey", id);
    }

    public int updateByPrimaryKeySelective(SysUserRoleRef record) {
        return super.getSqlSession().update("SysUserRoleRefMapper.updateByPrimaryKeySelective", record);
    }

    public int updateByPrimaryKey(SysUserRoleRef record) {
        return super.getSqlSession().update("SysUserRoleRefMapper.updateByPrimaryKey", record);
    }


    public List<SysRole> queryListToRole(SysUserRoleRefQuery example) {
        return super.getSqlSession().selectList("SysUserRoleRefMapper.selectByExampleToRole", example);
    }

}
