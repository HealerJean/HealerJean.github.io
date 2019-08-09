/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.dao.mybatis.dao.flow;


import com.hlj.proj.data.dao.mybatis.dao.BaseDao;
import com.hlj.proj.data.pojo.flow.ScfFlowAuditRecordTemp;
import com.hlj.proj.data.pojo.flow.ScfFlowAuditRecordTempQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("scfFlowAuditRecordTempDao")
public class ScfFlowAuditRecordTempDao extends BaseDao {

	public int countByExample(ScfFlowAuditRecordTempQuery example) {
		return super.getSqlSession().selectOne("ScfFlowAuditRecordTempMapper.countByExample", example);
	}

	public int deleteByExample(ScfFlowAuditRecordTempQuery example) {
		return super.getSqlSession().delete("ScfFlowAuditRecordTempMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("ScfFlowAuditRecordTempMapper.deleteByPrimaryKey", id);
	}

	public int insert(ScfFlowAuditRecordTemp record) {
		return super.getSqlSession().insert("ScfFlowAuditRecordTempMapper.insert", record);
	}

	public int insertSelective(ScfFlowAuditRecordTemp record) {
		return super.getSqlSession().insert("ScfFlowAuditRecordTempMapper.insertSelective", record);
	}

	public int batchInsert(List<ScfFlowAuditRecordTemp> list) {
		return super.batchInsert("ScfFlowAuditRecordTempMapper.insertSelective", list);
	}

	public List<ScfFlowAuditRecordTemp> selectByExample(ScfFlowAuditRecordTempQuery example) {
		return super.getSqlSession().selectList("ScfFlowAuditRecordTempMapper.selectByExample", example);
	}

	public List<ScfFlowAuditRecordTemp> selectPageByExample(ScfFlowAuditRecordTempQuery example) {
		return super.getSqlSession().selectList("ScfFlowAuditRecordTempMapper.selectPageByExample", example);
	}

	public ScfFlowAuditRecordTemp selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("ScfFlowAuditRecordTempMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(ScfFlowAuditRecordTemp record) {
		return super.getSqlSession().update("ScfFlowAuditRecordTempMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(ScfFlowAuditRecordTemp record) {
		return super.getSqlSession().update("ScfFlowAuditRecordTempMapper.updateByPrimaryKey", record);
	}

	public List<ScfFlowAuditRecordTemp> queryListGroupByFlowCode(ScfFlowAuditRecordTempQuery query) {
		return super.getSqlSession().selectList("ScfFlowAuditRecordTempMapper.queryListGroupByFlowCode", query);
	}

	public Integer queryListPageCount(ScfFlowAuditRecordTempQuery query) {
		return super.getSqlSession().selectOne("ScfFlowAuditRecordTempMapper.queryListPageCount", query);
	}

	public List<ScfFlowAuditRecordTemp> queryListPage(ScfFlowAuditRecordTempQuery query) {
		return super.getSqlSession().selectList("ScfFlowAuditRecordTempMapper.queryListPage", query);
	}
}
