/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.manager.flow;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.flow.FlowAuditDefaultConfigDao;
import com.healerjean.proj.data.pojo.flow.FlowAuditDefaultConfig;
import com.healerjean.proj.data.pojo.flow.FlowAuditDefaultConfigPage;
import com.healerjean.proj.data.pojo.flow.FlowAuditDefaultConfigQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowAuditDefaultConfigManager
 * @date 2099/1/1
 * @Description: FlowAuditDefaultConfigManager
 */
@Component("flowAuditDefaultConfigManager")
public class FlowAuditDefaultConfigManager {

	@Autowired
	@Qualifier("flowAuditDefaultConfigDao")
	private FlowAuditDefaultConfigDao flowAuditDefaultConfigDao;

	public FlowAuditDefaultConfigDao getDao() {
		return this.flowAuditDefaultConfigDao;
	}

	public int save(FlowAuditDefaultConfig flowAuditDefaultConfig) {
		int cnt = 0;
		if (flowAuditDefaultConfig.getId() == null) {
			cnt = flowAuditDefaultConfigDao.insertSelective(flowAuditDefaultConfig);
		} else {
			cnt = flowAuditDefaultConfigDao.updateByPrimaryKeySelective(flowAuditDefaultConfig);
		}
		return cnt;
	}

	public int update(FlowAuditDefaultConfig flowAuditDefaultConfig) {
		return flowAuditDefaultConfigDao.updateByPrimaryKey(flowAuditDefaultConfig);
	}

	public int updateSelective(FlowAuditDefaultConfig flowAuditDefaultConfig) {
		return flowAuditDefaultConfigDao.updateByPrimaryKeySelective(flowAuditDefaultConfig);
	}

	public int insert(FlowAuditDefaultConfig flowAuditDefaultConfig) {
		return flowAuditDefaultConfigDao.insert(flowAuditDefaultConfig);
	}

	public int insertSelective(FlowAuditDefaultConfig flowAuditDefaultConfig) {
		return flowAuditDefaultConfigDao.insertSelective(flowAuditDefaultConfig);
	}

	public int batchInsert(List<FlowAuditDefaultConfig> list){
		return flowAuditDefaultConfigDao.batchInsert(list);
	}

	public FlowAuditDefaultConfig findById(long id) {
		return flowAuditDefaultConfigDao.selectByPrimaryKey(id);
	}

	public FlowAuditDefaultConfig findByQueryContion(FlowAuditDefaultConfigQuery query) {
		List<FlowAuditDefaultConfig> list = flowAuditDefaultConfigDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<FlowAuditDefaultConfig> queryList(FlowAuditDefaultConfigQuery query) {
		return flowAuditDefaultConfigDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return flowAuditDefaultConfigDao.deleteByPrimaryKey(id);
	}

	public int delete(FlowAuditDefaultConfigQuery query) {
		return flowAuditDefaultConfigDao.deleteByExample(query);
	}

	public FlowAuditDefaultConfigPage queryPageList(FlowAuditDefaultConfigQuery query) {
		FlowAuditDefaultConfigPage flowAuditDefaultConfigPage = new FlowAuditDefaultConfigPage();
		Integer itemCount = flowAuditDefaultConfigDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			flowAuditDefaultConfigPage.setValues(null);
		} else {
			flowAuditDefaultConfigPage.setValues(flowAuditDefaultConfigDao.selectPageByExample(query));
		}

		flowAuditDefaultConfigPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return flowAuditDefaultConfigPage;
	}

}
