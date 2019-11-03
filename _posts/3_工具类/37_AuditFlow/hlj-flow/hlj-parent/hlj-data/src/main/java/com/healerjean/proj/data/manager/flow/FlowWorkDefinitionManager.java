/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.manager.flow;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.flow.FlowWorkDefinitionDao;
import com.healerjean.proj.data.pojo.flow.FlowWorkDefinition;
import com.healerjean.proj.data.pojo.flow.FlowWorkDefinitionPage;
import com.healerjean.proj.data.pojo.flow.FlowWorkDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowWorkDefinitionManager
 * @date 2099/1/1
 * @Description: FlowWorkDefinitionManager
 */
@Component("flowWorkDefinitionManager")
public class FlowWorkDefinitionManager {

	@Autowired
	@Qualifier("flowWorkDefinitionDao")
	private FlowWorkDefinitionDao flowWorkDefinitionDao;

	public FlowWorkDefinitionDao getDao() {
		return this.flowWorkDefinitionDao;
	}

	public int save(FlowWorkDefinition flowWorkDefinition) {
		int cnt = 0;
		if (flowWorkDefinition.getId() == null) {
			cnt = flowWorkDefinitionDao.insertSelective(flowWorkDefinition);
		} else {
			cnt = flowWorkDefinitionDao.updateByPrimaryKeySelective(flowWorkDefinition);
		}
		return cnt;
	}

	public int update(FlowWorkDefinition flowWorkDefinition) {
		return flowWorkDefinitionDao.updateByPrimaryKey(flowWorkDefinition);
	}

	public int updateSelective(FlowWorkDefinition flowWorkDefinition) {
		return flowWorkDefinitionDao.updateByPrimaryKeySelective(flowWorkDefinition);
	}

	public int insert(FlowWorkDefinition flowWorkDefinition) {
		return flowWorkDefinitionDao.insert(flowWorkDefinition);
	}

	public int insertSelective(FlowWorkDefinition flowWorkDefinition) {
		return flowWorkDefinitionDao.insertSelective(flowWorkDefinition);
	}

	public int batchInsert(List<FlowWorkDefinition> list){
		return flowWorkDefinitionDao.batchInsert(list);
	}

	public FlowWorkDefinition findById(long id) {
		return flowWorkDefinitionDao.selectByPrimaryKey(id);
	}

	public FlowWorkDefinition findByQueryContion(FlowWorkDefinitionQuery query) {
		List<FlowWorkDefinition> list = flowWorkDefinitionDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<FlowWorkDefinition> queryList(FlowWorkDefinitionQuery query) {
		return flowWorkDefinitionDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return flowWorkDefinitionDao.deleteByPrimaryKey(id);
	}

	public int delete(FlowWorkDefinitionQuery query) {
		return flowWorkDefinitionDao.deleteByExample(query);
	}

	public FlowWorkDefinitionPage queryPageList(FlowWorkDefinitionQuery query) {
		FlowWorkDefinitionPage flowWorkDefinitionPage = new FlowWorkDefinitionPage();
		Integer itemCount = flowWorkDefinitionDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			flowWorkDefinitionPage.setValues(null);
		} else {
			flowWorkDefinitionPage.setValues(flowWorkDefinitionDao.selectPageByExample(query));
		}

		flowWorkDefinitionPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return flowWorkDefinitionPage;
	}

}
