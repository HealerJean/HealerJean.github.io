/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.manager.flow;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.flow.FlowAuditUserDetailDao;
import com.healerjean.proj.data.pojo.flow.FlowAuditUserDetail;
import com.healerjean.proj.data.pojo.flow.FlowAuditUserDetailPage;
import com.healerjean.proj.data.pojo.flow.FlowAuditUserDetailQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowAuditUserDetailManager
 * @date 2099/1/1
 * @Description: FlowAuditUserDetailManager
 */
@Component("flowAuditUserDetailManager")
public class FlowAuditUserDetailManager {

	@Autowired
	@Qualifier("flowAuditUserDetailDao")
	private FlowAuditUserDetailDao flowAuditUserDetailDao;

	public FlowAuditUserDetailDao getDao() {
		return this.flowAuditUserDetailDao;
	}

	public int save(FlowAuditUserDetail flowAuditUserDetail) {
		int cnt = 0;
		if (flowAuditUserDetail.getId() == null) {
			cnt = flowAuditUserDetailDao.insertSelective(flowAuditUserDetail);
		} else {
			cnt = flowAuditUserDetailDao.updateByPrimaryKeySelective(flowAuditUserDetail);
		}
		return cnt;
	}

	public int update(FlowAuditUserDetail flowAuditUserDetail) {
		return flowAuditUserDetailDao.updateByPrimaryKey(flowAuditUserDetail);
	}

	public int updateSelective(FlowAuditUserDetail flowAuditUserDetail) {
		return flowAuditUserDetailDao.updateByPrimaryKeySelective(flowAuditUserDetail);
	}

	public int insert(FlowAuditUserDetail flowAuditUserDetail) {
		return flowAuditUserDetailDao.insert(flowAuditUserDetail);
	}

	public int insertSelective(FlowAuditUserDetail flowAuditUserDetail) {
		return flowAuditUserDetailDao.insertSelective(flowAuditUserDetail);
	}

	public int batchInsert(List<FlowAuditUserDetail> list){
		return flowAuditUserDetailDao.batchInsert(list);
	}

	public FlowAuditUserDetail findById(long id) {
		return flowAuditUserDetailDao.selectByPrimaryKey(id);
	}

	public FlowAuditUserDetail findByQueryContion(FlowAuditUserDetailQuery query) {
		List<FlowAuditUserDetail> list = flowAuditUserDetailDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<FlowAuditUserDetail> queryList(FlowAuditUserDetailQuery query) {
		return flowAuditUserDetailDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return flowAuditUserDetailDao.deleteByPrimaryKey(id);
	}

	public int delete(FlowAuditUserDetailQuery query) {
		return flowAuditUserDetailDao.deleteByExample(query);
	}

	public FlowAuditUserDetailPage queryPageList(FlowAuditUserDetailQuery query) {
		FlowAuditUserDetailPage flowAuditUserDetailPage = new FlowAuditUserDetailPage();
		Integer itemCount = flowAuditUserDetailDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			flowAuditUserDetailPage.setValues(null);
		} else {
			flowAuditUserDetailPage.setValues(flowAuditUserDetailDao.selectPageByExample(query));
		}

		flowAuditUserDetailPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return flowAuditUserDetailPage;
	}

}
