/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.flow;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.flow.FlowWorkDefaultDefinition;
import com.healerjean.proj.data.pojo.flow.FlowWorkDefaultDefinitionQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowWorkDefaultDefinitionDao
 * @date 2099/1/1
 * @Description: FlowWorkDefaultDefinitionDao
 */
@Repository("flowWorkDefaultDefinitionDao")
public class FlowWorkDefaultDefinitionDao extends BaseDao {

	public int countByExample(FlowWorkDefaultDefinitionQuery example) {
		return super.getSqlSession().selectOne("FlowWorkDefaultDefinitionMapper.countByExample", example);
	}

	public int deleteByExample(FlowWorkDefaultDefinitionQuery example) {
		return super.getSqlSession().delete("FlowWorkDefaultDefinitionMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("FlowWorkDefaultDefinitionMapper.deleteByPrimaryKey", id);
	}

	public int insert(FlowWorkDefaultDefinition record) {
		return super.getSqlSession().insert("FlowWorkDefaultDefinitionMapper.insert", record);
	}

	public int insertSelective(FlowWorkDefaultDefinition record) {
		return super.getSqlSession().insert("FlowWorkDefaultDefinitionMapper.insertSelective", record);
	}

	public int batchInsert(List<FlowWorkDefaultDefinition> list) {
		return super.batchInsert("FlowWorkDefaultDefinitionMapper.insertSelective", list);
	}

	public List<FlowWorkDefaultDefinition> selectByExample(FlowWorkDefaultDefinitionQuery example) {
		return super.getSqlSession().selectList("FlowWorkDefaultDefinitionMapper.selectByExample", example);
	}

	public List<FlowWorkDefaultDefinition> selectPageByExample(FlowWorkDefaultDefinitionQuery example) {
		return super.getSqlSession().selectList("FlowWorkDefaultDefinitionMapper.selectPageByExample", example);
	}

	public FlowWorkDefaultDefinition selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("FlowWorkDefaultDefinitionMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(FlowWorkDefaultDefinition record) {
		return super.getSqlSession().update("FlowWorkDefaultDefinitionMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(FlowWorkDefaultDefinition record) {
		return super.getSqlSession().update("FlowWorkDefaultDefinitionMapper.updateByPrimaryKey", record);
	}

}
