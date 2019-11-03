/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.flow;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.flow.FlowWorkNode;
import com.healerjean.proj.data.pojo.flow.FlowWorkNodeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowWorkNodeDao
 * @date 2099/1/1
 * @Description: FlowWorkNodeDao
 */
@Repository("flowWorkNodeDao")
public class FlowWorkNodeDao extends BaseDao {

	public int countByExample(FlowWorkNodeQuery example) {
		return super.getSqlSession().selectOne("FlowWorkNodeMapper.countByExample", example);
	}

	public int deleteByExample(FlowWorkNodeQuery example) {
		return super.getSqlSession().delete("FlowWorkNodeMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("FlowWorkNodeMapper.deleteByPrimaryKey", id);
	}

	public int insert(FlowWorkNode record) {
		return super.getSqlSession().insert("FlowWorkNodeMapper.insert", record);
	}

	public int insertSelective(FlowWorkNode record) {
		return super.getSqlSession().insert("FlowWorkNodeMapper.insertSelective", record);
	}

	public int batchInsert(List<FlowWorkNode> list) {
		return super.batchInsert("FlowWorkNodeMapper.insertSelective", list);
	}

	public List<FlowWorkNode> selectByExample(FlowWorkNodeQuery example) {
		return super.getSqlSession().selectList("FlowWorkNodeMapper.selectByExample", example);
	}

	public List<FlowWorkNode> selectPageByExample(FlowWorkNodeQuery example) {
		return super.getSqlSession().selectList("FlowWorkNodeMapper.selectPageByExample", example);
	}

	public FlowWorkNode selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("FlowWorkNodeMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(FlowWorkNode record) {
		return super.getSqlSession().update("FlowWorkNodeMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(FlowWorkNode record) {
		return super.getSqlSession().update("FlowWorkNodeMapper.updateByPrimaryKey", record);
	}

}
