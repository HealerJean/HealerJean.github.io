/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.dao.system;

import java.util.List;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.system.SysUserInfo;
import com.healerjean.proj.data.pojo.system.SysUserInfoQuery;
import org.springframework.stereotype.Repository;

/**
 * @author zhangyujin
 * @ClassName: SysUserInfoDao
 * @date 2099/1/1
 * @Description: SysUserInfoDao
 */
@Repository("sysUserInfoDao")
public class SysUserInfoDao extends BaseDao {

    public int countByExample(SysUserInfoQuery example) {
        return super.getSqlSession().selectOne("SysUserInfoMapper.countByExample", example);
    }

    public int deleteByExample(SysUserInfoQuery example) {
        return super.getSqlSession().delete("SysUserInfoMapper.deleteByExample", example);
    }

    public int deleteByPrimaryKey(long id) {
        return super.getSqlSession().delete("SysUserInfoMapper.deleteByPrimaryKey", id);
    }

    public int insert(SysUserInfo record) {
        return super.getSqlSession().insert("SysUserInfoMapper.insert", record);
    }

    public int insertSelective(SysUserInfo record) {
        return super.getSqlSession().insert("SysUserInfoMapper.insertSelective", record);
    }

    public int batchInsert(List<SysUserInfo> list) {
        return super.batchInsert("SysUserInfoMapper.insertSelective", list);
    }

    public List<SysUserInfo> selectByExample(SysUserInfoQuery example) {
        return super.getSqlSession().selectList("SysUserInfoMapper.selectByExample", example);
    }

    public List<SysUserInfo> selectPageByExample(SysUserInfoQuery example) {
        return super.getSqlSession().selectList("SysUserInfoMapper.selectPageByExample", example);
    }

    public SysUserInfo selectByPrimaryKey(long id) {
        return super.getSqlSession().selectOne("SysUserInfoMapper.selectByPrimaryKey", id);
    }

    public int updateByPrimaryKeySelective(SysUserInfo record) {
        return super.getSqlSession().update("SysUserInfoMapper.updateByPrimaryKeySelective", record);
    }

    public int updateByPrimaryKey(SysUserInfo record) {
        return super.getSqlSession().update("SysUserInfoMapper.updateByPrimaryKey", record);
    }

}
