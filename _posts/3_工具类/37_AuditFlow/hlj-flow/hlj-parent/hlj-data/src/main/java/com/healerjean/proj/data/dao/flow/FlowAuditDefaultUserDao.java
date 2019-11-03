/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.flow;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.flow.FlowAuditDefaultUser;
import com.healerjean.proj.data.pojo.flow.FlowAuditDefaultUserQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowAuditDefaultUserDao
 * @date 2099/1/1
 * @Description: FlowAuditDefaultUserDao
 */
@Repository("flowAuditDefaultUserDao")
public class FlowAuditDefaultUserDao extends BaseDao {

	public int countByExample(FlowAuditDefaultUserQuery example) {
		return super.getSqlSession().selectOne("FlowAuditDefaultUserMapper.countByExample", example);
	}

	public int deleteByExample(FlowAuditDefaultUserQuery example) {
		return super.getSqlSession().delete("FlowAuditDefaultUserMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("FlowAuditDefaultUserMapper.deleteByPrimaryKey", id);
	}

	public int insert(FlowAuditDefaultUser record) {
		return super.getSqlSession().insert("FlowAuditDefaultUserMapper.insert", record);
	}

	public int insertSelective(FlowAuditDefaultUser record) {
		return super.getSqlSession().insert("FlowAuditDefaultUserMapper.insertSelective", record);
	}

	public int batchInsert(List<FlowAuditDefaultUser> list) {
		return super.batchInsert("FlowAuditDefaultUserMapper.insertSelective", list);
	}

	public List<FlowAuditDefaultUser> selectByExample(FlowAuditDefaultUserQuery example) {
		return super.getSqlSession().selectList("FlowAuditDefaultUserMapper.selectByExample", example);
	}

	public List<FlowAuditDefaultUser> selectPageByExample(FlowAuditDefaultUserQuery example) {
		return super.getSqlSession().selectList("FlowAuditDefaultUserMapper.selectPageByExample", example);
	}

	public FlowAuditDefaultUser selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("FlowAuditDefaultUserMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(FlowAuditDefaultUser record) {
		return super.getSqlSession().update("FlowAuditDefaultUserMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(FlowAuditDefaultUser record) {
		return super.getSqlSession().update("FlowAuditDefaultUserMapper.updateByPrimaryKey", record);
	}

}
