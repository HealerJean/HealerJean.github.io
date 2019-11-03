/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.flow;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.flow.FlowAuditTask;
import com.healerjean.proj.data.pojo.flow.FlowAuditTaskQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowAuditTaskDao
 * @date 2099/1/1
 * @Description: FlowAuditTaskDao
 */
@Repository("flowAuditTaskDao")
public class FlowAuditTaskDao extends BaseDao {

	public int countByExample(FlowAuditTaskQuery example) {
		return super.getSqlSession().selectOne("FlowAuditTaskMapper.countByExample", example);
	}

	public int deleteByExample(FlowAuditTaskQuery example) {
		return super.getSqlSession().delete("FlowAuditTaskMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("FlowAuditTaskMapper.deleteByPrimaryKey", id);
	}

	public int insert(FlowAuditTask record) {
		return super.getSqlSession().insert("FlowAuditTaskMapper.insert", record);
	}

	public int insertSelective(FlowAuditTask record) {
		return super.getSqlSession().insert("FlowAuditTaskMapper.insertSelective", record);
	}

	public int batchInsert(List<FlowAuditTask> list) {
		return super.batchInsert("FlowAuditTaskMapper.insertSelective", list);
	}

	public List<FlowAuditTask> selectByExample(FlowAuditTaskQuery example) {
		return super.getSqlSession().selectList("FlowAuditTaskMapper.selectByExample", example);
	}

	public List<FlowAuditTask> selectPageByExample(FlowAuditTaskQuery example) {
		return super.getSqlSession().selectList("FlowAuditTaskMapper.selectPageByExample", example);
	}

	public FlowAuditTask selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("FlowAuditTaskMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(FlowAuditTask record) {
		return super.getSqlSession().update("FlowAuditTaskMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(FlowAuditTask record) {
		return super.getSqlSession().update("FlowAuditTaskMapper.updateByPrimaryKey", record);
	}

}
