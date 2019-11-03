/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.flow;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.flow.FlowAuditDefaultConfig;
import com.healerjean.proj.data.pojo.flow.FlowAuditDefaultConfigQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowAuditDefaultConfigDao
 * @date 2099/1/1
 * @Description: FlowAuditDefaultConfigDao
 */
@Repository("flowAuditDefaultConfigDao")
public class FlowAuditDefaultConfigDao extends BaseDao {

	public int countByExample(FlowAuditDefaultConfigQuery example) {
		return super.getSqlSession().selectOne("FlowAuditDefaultConfigMapper.countByExample", example);
	}

	public int deleteByExample(FlowAuditDefaultConfigQuery example) {
		return super.getSqlSession().delete("FlowAuditDefaultConfigMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("FlowAuditDefaultConfigMapper.deleteByPrimaryKey", id);
	}

	public int insert(FlowAuditDefaultConfig record) {
		return super.getSqlSession().insert("FlowAuditDefaultConfigMapper.insert", record);
	}

	public int insertSelective(FlowAuditDefaultConfig record) {
		return super.getSqlSession().insert("FlowAuditDefaultConfigMapper.insertSelective", record);
	}

	public int batchInsert(List<FlowAuditDefaultConfig> list) {
		return super.batchInsert("FlowAuditDefaultConfigMapper.insertSelective", list);
	}

	public List<FlowAuditDefaultConfig> selectByExample(FlowAuditDefaultConfigQuery example) {
		return super.getSqlSession().selectList("FlowAuditDefaultConfigMapper.selectByExample", example);
	}

	public List<FlowAuditDefaultConfig> selectPageByExample(FlowAuditDefaultConfigQuery example) {
		return super.getSqlSession().selectList("FlowAuditDefaultConfigMapper.selectPageByExample", example);
	}

	public FlowAuditDefaultConfig selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("FlowAuditDefaultConfigMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(FlowAuditDefaultConfig record) {
		return super.getSqlSession().update("FlowAuditDefaultConfigMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(FlowAuditDefaultConfig record) {
		return super.getSqlSession().update("FlowAuditDefaultConfigMapper.updateByPrimaryKey", record);
	}

}
