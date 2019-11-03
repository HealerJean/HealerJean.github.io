/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.flow;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.flow.FlowWorkDefinition;
import com.healerjean.proj.data.pojo.flow.FlowWorkDefinitionQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowWorkDefinitionDao
 * @date 2099/1/1
 * @Description: FlowWorkDefinitionDao
 */
@Repository("flowWorkDefinitionDao")
public class FlowWorkDefinitionDao extends BaseDao {

	public int countByExample(FlowWorkDefinitionQuery example) {
		return super.getSqlSession().selectOne("FlowWorkDefinitionMapper.countByExample", example);
	}

	public int deleteByExample(FlowWorkDefinitionQuery example) {
		return super.getSqlSession().delete("FlowWorkDefinitionMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("FlowWorkDefinitionMapper.deleteByPrimaryKey", id);
	}

	public int insert(FlowWorkDefinition record) {
		return super.getSqlSession().insert("FlowWorkDefinitionMapper.insert", record);
	}

	public int insertSelective(FlowWorkDefinition record) {
		return super.getSqlSession().insert("FlowWorkDefinitionMapper.insertSelective", record);
	}

	public int batchInsert(List<FlowWorkDefinition> list) {
		return super.batchInsert("FlowWorkDefinitionMapper.insertSelective", list);
	}

	public List<FlowWorkDefinition> selectByExample(FlowWorkDefinitionQuery example) {
		return super.getSqlSession().selectList("FlowWorkDefinitionMapper.selectByExample", example);
	}

	public List<FlowWorkDefinition> selectPageByExample(FlowWorkDefinitionQuery example) {
		return super.getSqlSession().selectList("FlowWorkDefinitionMapper.selectPageByExample", example);
	}

	public FlowWorkDefinition selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("FlowWorkDefinitionMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(FlowWorkDefinition record) {
		return super.getSqlSession().update("FlowWorkDefinitionMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(FlowWorkDefinition record) {
		return super.getSqlSession().update("FlowWorkDefinitionMapper.updateByPrimaryKey", record);
	}

}
