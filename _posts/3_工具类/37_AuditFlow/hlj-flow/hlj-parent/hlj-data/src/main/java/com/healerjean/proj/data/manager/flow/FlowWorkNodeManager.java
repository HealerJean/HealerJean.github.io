/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.manager.flow;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.flow.FlowWorkNodeDao;
import com.healerjean.proj.data.pojo.flow.FlowWorkNode;
import com.healerjean.proj.data.pojo.flow.FlowWorkNodePage;
import com.healerjean.proj.data.pojo.flow.FlowWorkNodeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowWorkNodeManager
 * @date 2099/1/1
 * @Description: FlowWorkNodeManager
 */
@Component("flowWorkNodeManager")
public class FlowWorkNodeManager {

	@Autowired
	@Qualifier("flowWorkNodeDao")
	private FlowWorkNodeDao flowWorkNodeDao;

	public FlowWorkNodeDao getDao() {
		return this.flowWorkNodeDao;
	}

	public int save(FlowWorkNode flowWorkNode) {
		int cnt = 0;
		if (flowWorkNode.getId() == null) {
			cnt = flowWorkNodeDao.insertSelective(flowWorkNode);
		} else {
			cnt = flowWorkNodeDao.updateByPrimaryKeySelective(flowWorkNode);
		}
		return cnt;
	}

	public int update(FlowWorkNode flowWorkNode) {
		return flowWorkNodeDao.updateByPrimaryKey(flowWorkNode);
	}

	public int updateSelective(FlowWorkNode flowWorkNode) {
		return flowWorkNodeDao.updateByPrimaryKeySelective(flowWorkNode);
	}

	public int insert(FlowWorkNode flowWorkNode) {
		return flowWorkNodeDao.insert(flowWorkNode);
	}

	public int insertSelective(FlowWorkNode flowWorkNode) {
		return flowWorkNodeDao.insertSelective(flowWorkNode);
	}

	public int batchInsert(List<FlowWorkNode> list){
		return flowWorkNodeDao.batchInsert(list);
	}

	public FlowWorkNode findById(long id) {
		return flowWorkNodeDao.selectByPrimaryKey(id);
	}

	public FlowWorkNode findByQueryContion(FlowWorkNodeQuery query) {
		List<FlowWorkNode> list = flowWorkNodeDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<FlowWorkNode> queryList(FlowWorkNodeQuery query) {
		return flowWorkNodeDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return flowWorkNodeDao.deleteByPrimaryKey(id);
	}

	public int delete(FlowWorkNodeQuery query) {
		return flowWorkNodeDao.deleteByExample(query);
	}

	public FlowWorkNodePage queryPageList(FlowWorkNodeQuery query) {
		FlowWorkNodePage flowWorkNodePage = new FlowWorkNodePage();
		Integer itemCount = flowWorkNodeDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			flowWorkNodePage.setValues(null);
		} else {
			flowWorkNodePage.setValues(flowWorkNodeDao.selectPageByExample(query));
		}

		flowWorkNodePage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return flowWorkNodePage;
	}

}
