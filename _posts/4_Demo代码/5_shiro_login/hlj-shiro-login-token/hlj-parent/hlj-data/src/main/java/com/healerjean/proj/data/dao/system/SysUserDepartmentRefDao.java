/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.dao.system;

import java.util.List;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.system.SysDepartment;
import com.healerjean.proj.data.pojo.system.SysUserDepartmentRef;
import com.healerjean.proj.data.pojo.system.SysUserDepartmentRefQuery;
import org.springframework.stereotype.Repository;

/**
 * @author zhangyujin
 * @ClassName: SysUserDepartmentRefDao
 * @date 2099/1/1
 * @Description: SysUserDepartmentRefDao
 */
@Repository("sysUserDepartmentRefDao")
public class SysUserDepartmentRefDao extends BaseDao {

    public int countByExample(SysUserDepartmentRefQuery example) {
        return super.getSqlSession().selectOne("SysUserDepartmentRefMapper.countByExample", example);
    }

    public int deleteByExample(SysUserDepartmentRefQuery example) {
        return super.getSqlSession().delete("SysUserDepartmentRefMapper.deleteByExample", example);
    }

    public int deleteByPrimaryKey(long id) {
        return super.getSqlSession().delete("SysUserDepartmentRefMapper.deleteByPrimaryKey", id);
    }

    public int insert(SysUserDepartmentRef record) {
        return super.getSqlSession().insert("SysUserDepartmentRefMapper.insert", record);
    }

    public int insertSelective(SysUserDepartmentRef record) {
        return super.getSqlSession().insert("SysUserDepartmentRefMapper.insertSelective", record);
    }

    public int batchInsert(List<SysUserDepartmentRef> list) {
        return super.batchInsert("SysUserDepartmentRefMapper.insertSelective", list);
    }

    public List<SysUserDepartmentRef> selectByExample(SysUserDepartmentRefQuery example) {
        return super.getSqlSession().selectList("SysUserDepartmentRefMapper.selectByExample", example);
    }

    public List<SysUserDepartmentRef> selectPageByExample(SysUserDepartmentRefQuery example) {
        return super.getSqlSession().selectList("SysUserDepartmentRefMapper.selectPageByExample", example);
    }

    public SysUserDepartmentRef selectByPrimaryKey(long id) {
        return super.getSqlSession().selectOne("SysUserDepartmentRefMapper.selectByPrimaryKey", id);
    }

    public int updateByPrimaryKeySelective(SysUserDepartmentRef record) {
        return super.getSqlSession().update("SysUserDepartmentRefMapper.updateByPrimaryKeySelective", record);
    }

    public int updateByPrimaryKey(SysUserDepartmentRef record) {
        return super.getSqlSession().update("SysUserDepartmentRefMapper.updateByPrimaryKey", record);
    }


    public SysDepartment selectByExampleToUser(SysUserDepartmentRefQuery query) {
        return super.getSqlSession().selectOne("SysUserDepartmentRefMapper.selectByExampleToUser", query);
    }

}
