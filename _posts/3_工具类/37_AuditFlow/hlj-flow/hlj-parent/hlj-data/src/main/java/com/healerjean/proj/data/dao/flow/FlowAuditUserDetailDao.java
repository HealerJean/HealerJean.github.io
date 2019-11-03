/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.flow;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.flow.FlowAuditUserDetail;
import com.healerjean.proj.data.pojo.flow.FlowAuditUserDetailQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowAuditUserDetailDao
 * @date 2099/1/1
 * @Description: FlowAuditUserDetailDao
 */
@Repository("flowAuditUserDetailDao")
public class FlowAuditUserDetailDao extends BaseDao {

	public int countByExample(FlowAuditUserDetailQuery example) {
		return super.getSqlSession().selectOne("FlowAuditUserDetailMapper.countByExample", example);
	}

	public int deleteByExample(FlowAuditUserDetailQuery example) {
		return super.getSqlSession().delete("FlowAuditUserDetailMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("FlowAuditUserDetailMapper.deleteByPrimaryKey", id);
	}

	public int insert(FlowAuditUserDetail record) {
		return super.getSqlSession().insert("FlowAuditUserDetailMapper.insert", record);
	}

	public int insertSelective(FlowAuditUserDetail record) {
		return super.getSqlSession().insert("FlowAuditUserDetailMapper.insertSelective", record);
	}

	public int batchInsert(List<FlowAuditUserDetail> list) {
		return super.batchInsert("FlowAuditUserDetailMapper.insertSelective", list);
	}

	public List<FlowAuditUserDetail> selectByExample(FlowAuditUserDetailQuery example) {
		return super.getSqlSession().selectList("FlowAuditUserDetailMapper.selectByExample", example);
	}

	public List<FlowAuditUserDetail> selectPageByExample(FlowAuditUserDetailQuery example) {
		return super.getSqlSession().selectList("FlowAuditUserDetailMapper.selectPageByExample", example);
	}

	public FlowAuditUserDetail selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("FlowAuditUserDetailMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(FlowAuditUserDetail record) {
		return super.getSqlSession().update("FlowAuditUserDetailMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(FlowAuditUserDetail record) {
		return super.getSqlSession().update("FlowAuditUserDetailMapper.updateByPrimaryKey", record);
	}

}
