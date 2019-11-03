/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.manager.flow;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.flow.FlowAuditTaskDao;
import com.healerjean.proj.data.pojo.flow.FlowAuditTask;
import com.healerjean.proj.data.pojo.flow.FlowAuditTaskPage;
import com.healerjean.proj.data.pojo.flow.FlowAuditTaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowAuditTaskManager
 * @date 2099/1/1
 * @Description: FlowAuditTaskManager
 */
@Component("flowAuditTaskManager")
public class FlowAuditTaskManager {

	@Autowired
	@Qualifier("flowAuditTaskDao")
	private FlowAuditTaskDao flowAuditTaskDao;

	public FlowAuditTaskDao getDao() {
		return this.flowAuditTaskDao;
	}

	public int save(FlowAuditTask flowAuditTask) {
		int cnt = 0;
		if (flowAuditTask.getId() == null) {
			cnt = flowAuditTaskDao.insertSelective(flowAuditTask);
		} else {
			cnt = flowAuditTaskDao.updateByPrimaryKeySelective(flowAuditTask);
		}
		return cnt;
	}

	public int update(FlowAuditTask flowAuditTask) {
		return flowAuditTaskDao.updateByPrimaryKey(flowAuditTask);
	}

	public int updateSelective(FlowAuditTask flowAuditTask) {
		return flowAuditTaskDao.updateByPrimaryKeySelective(flowAuditTask);
	}

	public int insert(FlowAuditTask flowAuditTask) {
		return flowAuditTaskDao.insert(flowAuditTask);
	}

	public int insertSelective(FlowAuditTask flowAuditTask) {
		return flowAuditTaskDao.insertSelective(flowAuditTask);
	}

	public int batchInsert(List<FlowAuditTask> list){
		return flowAuditTaskDao.batchInsert(list);
	}

	public FlowAuditTask findById(long id) {
		return flowAuditTaskDao.selectByPrimaryKey(id);
	}

	public FlowAuditTask findByQueryContion(FlowAuditTaskQuery query) {
		List<FlowAuditTask> list = flowAuditTaskDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<FlowAuditTask> queryList(FlowAuditTaskQuery query) {
		return flowAuditTaskDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return flowAuditTaskDao.deleteByPrimaryKey(id);
	}

	public int delete(FlowAuditTaskQuery query) {
		return flowAuditTaskDao.deleteByExample(query);
	}

	public FlowAuditTaskPage queryPageList(FlowAuditTaskQuery query) {
		FlowAuditTaskPage flowAuditTaskPage = new FlowAuditTaskPage();
		Integer itemCount = flowAuditTaskDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			flowAuditTaskPage.setValues(null);
		} else {
			flowAuditTaskPage.setValues(flowAuditTaskDao.selectPageByExample(query));
		}

		flowAuditTaskPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return flowAuditTaskPage;
	}

}
