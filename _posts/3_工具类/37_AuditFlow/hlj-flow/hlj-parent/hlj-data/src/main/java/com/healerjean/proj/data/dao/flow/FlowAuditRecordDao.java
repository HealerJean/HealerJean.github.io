/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.flow;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.flow.FlowAuditRecord;
import com.healerjean.proj.data.pojo.flow.FlowAuditRecordQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowAuditRecordDao
 * @date 2099/1/1
 * @Description: FlowAuditRecordDao
 */
@Repository("flowAuditRecordDao")
public class FlowAuditRecordDao extends BaseDao {

	public int countByExample(FlowAuditRecordQuery example) {
		return super.getSqlSession().selectOne("FlowAuditRecordMapper.countByExample", example);
	}

	public int deleteByExample(FlowAuditRecordQuery example) {
		return super.getSqlSession().delete("FlowAuditRecordMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("FlowAuditRecordMapper.deleteByPrimaryKey", id);
	}

	public int insert(FlowAuditRecord record) {
		return super.getSqlSession().insert("FlowAuditRecordMapper.insert", record);
	}

	public int insertSelective(FlowAuditRecord record) {
		return super.getSqlSession().insert("FlowAuditRecordMapper.insertSelective", record);
	}

	public int batchInsert(List<FlowAuditRecord> list) {
		return super.batchInsert("FlowAuditRecordMapper.insertSelective", list);
	}

	public List<FlowAuditRecord> selectByExample(FlowAuditRecordQuery example) {
		return super.getSqlSession().selectList("FlowAuditRecordMapper.selectByExample", example);
	}

	public List<FlowAuditRecord> selectPageByExample(FlowAuditRecordQuery example) {
		return super.getSqlSession().selectList("FlowAuditRecordMapper.selectPageByExample", example);
	}

	public FlowAuditRecord selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("FlowAuditRecordMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(FlowAuditRecord record) {
		return super.getSqlSession().update("FlowAuditRecordMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(FlowAuditRecord record) {
		return super.getSqlSession().update("FlowAuditRecordMapper.updateByPrimaryKey", record);
	}

}
