/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.flow;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.flow.FlowWorkRecord;
import com.healerjean.proj.data.pojo.flow.FlowWorkRecordQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowWorkRecordDao
 * @date 2099/1/1
 * @Description: FlowWorkRecordDao
 */
@Repository("flowWorkRecordDao")
public class FlowWorkRecordDao extends BaseDao {

	public int countByExample(FlowWorkRecordQuery example) {
		return super.getSqlSession().selectOne("FlowWorkRecordMapper.countByExample", example);
	}

	public int deleteByExample(FlowWorkRecordQuery example) {
		return super.getSqlSession().delete("FlowWorkRecordMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("FlowWorkRecordMapper.deleteByPrimaryKey", id);
	}

	public int insert(FlowWorkRecord record) {
		return super.getSqlSession().insert("FlowWorkRecordMapper.insert", record);
	}

	public int insertSelective(FlowWorkRecord record) {
		return super.getSqlSession().insert("FlowWorkRecordMapper.insertSelective", record);
	}

	public int batchInsert(List<FlowWorkRecord> list) {
		return super.batchInsert("FlowWorkRecordMapper.insertSelective", list);
	}

	public List<FlowWorkRecord> selectByExample(FlowWorkRecordQuery example) {
		return super.getSqlSession().selectList("FlowWorkRecordMapper.selectByExample", example);
	}

	public List<FlowWorkRecord> selectPageByExample(FlowWorkRecordQuery example) {
		return super.getSqlSession().selectList("FlowWorkRecordMapper.selectPageByExample", example);
	}

	public FlowWorkRecord selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("FlowWorkRecordMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(FlowWorkRecord record) {
		return super.getSqlSession().update("FlowWorkRecordMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(FlowWorkRecord record) {
		return super.getSqlSession().update("FlowWorkRecordMapper.updateByPrimaryKey", record);
	}

}
