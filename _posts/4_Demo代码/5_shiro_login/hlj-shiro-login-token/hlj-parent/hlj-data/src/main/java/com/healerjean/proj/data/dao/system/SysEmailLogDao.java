/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.dao.system;

import java.util.List;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.system.SysEmailLog;
import com.healerjean.proj.data.pojo.system.SysEmailLogQuery;
import org.springframework.stereotype.Repository;

/**
 * @author zhangyujin
 * @ClassName: SysEmailLogDao
 * @date 2099/1/1
 * @Description: SysEmailLogDao
 */
@Repository("sysEmailLogDao")
public class SysEmailLogDao extends BaseDao {

    public int countByExample(SysEmailLogQuery example) {
        return super.getSqlSession().selectOne("SysEmailLogMapper.countByExample", example);
    }

    public int deleteByExample(SysEmailLogQuery example) {
        return super.getSqlSession().delete("SysEmailLogMapper.deleteByExample", example);
    }

    public int deleteByPrimaryKey(long id) {
        return super.getSqlSession().delete("SysEmailLogMapper.deleteByPrimaryKey", id);
    }

    public int insert(SysEmailLog record) {
        return super.getSqlSession().insert("SysEmailLogMapper.insert", record);
    }

    public int insertSelective(SysEmailLog record) {
        return super.getSqlSession().insert("SysEmailLogMapper.insertSelective", record);
    }

    public int batchInsert(List<SysEmailLog> list) {
        return super.batchInsert("SysEmailLogMapper.insertSelective", list);
    }

    public List<SysEmailLog> selectByExample(SysEmailLogQuery example) {
        return super.getSqlSession().selectList("SysEmailLogMapper.selectByExample", example);
    }

    public List<SysEmailLog> selectPageByExample(SysEmailLogQuery example) {
        return super.getSqlSession().selectList("SysEmailLogMapper.selectPageByExample", example);
    }

    public SysEmailLog selectByPrimaryKey(long id) {
        return super.getSqlSession().selectOne("SysEmailLogMapper.selectByPrimaryKey", id);
    }

    public int updateByPrimaryKeySelective(SysEmailLog record) {
        return super.getSqlSession().update("SysEmailLogMapper.updateByPrimaryKeySelective", record);
    }

    public int updateByPrimaryKey(SysEmailLog record) {
        return super.getSqlSession().update("SysEmailLogMapper.updateByPrimaryKey", record);
    }

}
