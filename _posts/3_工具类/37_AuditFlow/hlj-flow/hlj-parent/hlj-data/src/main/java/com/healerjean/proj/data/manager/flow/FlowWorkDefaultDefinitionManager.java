/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.manager.flow;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.flow.FlowWorkDefaultDefinitionDao;
import com.healerjean.proj.data.pojo.flow.FlowWorkDefaultDefinition;
import com.healerjean.proj.data.pojo.flow.FlowWorkDefaultDefinitionPage;
import com.healerjean.proj.data.pojo.flow.FlowWorkDefaultDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowWorkDefaultDefinitionManager
 * @date 2099/1/1
 * @Description: FlowWorkDefaultDefinitionManager
 */
@Component("flowWorkDefaultDefinitionManager")
public class FlowWorkDefaultDefinitionManager {

	@Autowired
	@Qualifier("flowWorkDefaultDefinitionDao")
	private FlowWorkDefaultDefinitionDao flowWorkDefaultDefinitionDao;

	public FlowWorkDefaultDefinitionDao getDao() {
		return this.flowWorkDefaultDefinitionDao;
	}

	public int save(FlowWorkDefaultDefinition flowWorkDefaultDefinition) {
		int cnt = 0;
		if (flowWorkDefaultDefinition.getId() == null) {
			cnt = flowWorkDefaultDefinitionDao.insertSelective(flowWorkDefaultDefinition);
		} else {
			cnt = flowWorkDefaultDefinitionDao.updateByPrimaryKeySelective(flowWorkDefaultDefinition);
		}
		return cnt;
	}

	public int update(FlowWorkDefaultDefinition flowWorkDefaultDefinition) {
		return flowWorkDefaultDefinitionDao.updateByPrimaryKey(flowWorkDefaultDefinition);
	}

	public int updateSelective(FlowWorkDefaultDefinition flowWorkDefaultDefinition) {
		return flowWorkDefaultDefinitionDao.updateByPrimaryKeySelective(flowWorkDefaultDefinition);
	}

	public int insert(FlowWorkDefaultDefinition flowWorkDefaultDefinition) {
		return flowWorkDefaultDefinitionDao.insert(flowWorkDefaultDefinition);
	}

	public int insertSelective(FlowWorkDefaultDefinition flowWorkDefaultDefinition) {
		return flowWorkDefaultDefinitionDao.insertSelective(flowWorkDefaultDefinition);
	}

	public int batchInsert(List<FlowWorkDefaultDefinition> list){
		return flowWorkDefaultDefinitionDao.batchInsert(list);
	}

	public FlowWorkDefaultDefinition findById(long id) {
		return flowWorkDefaultDefinitionDao.selectByPrimaryKey(id);
	}

	public FlowWorkDefaultDefinition findByQueryContion(FlowWorkDefaultDefinitionQuery query) {
		List<FlowWorkDefaultDefinition> list = flowWorkDefaultDefinitionDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<FlowWorkDefaultDefinition> queryList(FlowWorkDefaultDefinitionQuery query) {
		return flowWorkDefaultDefinitionDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return flowWorkDefaultDefinitionDao.deleteByPrimaryKey(id);
	}

	public int delete(FlowWorkDefaultDefinitionQuery query) {
		return flowWorkDefaultDefinitionDao.deleteByExample(query);
	}

	public FlowWorkDefaultDefinitionPage queryPageList(FlowWorkDefaultDefinitionQuery query) {
		FlowWorkDefaultDefinitionPage flowWorkDefaultDefinitionPage = new FlowWorkDefaultDefinitionPage();
		Integer itemCount = flowWorkDefaultDefinitionDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			flowWorkDefaultDefinitionPage.setValues(null);
		} else {
			flowWorkDefaultDefinitionPage.setValues(flowWorkDefaultDefinitionDao.selectPageByExample(query));
		}

		flowWorkDefaultDefinitionPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return flowWorkDefaultDefinitionPage;
	}

}
