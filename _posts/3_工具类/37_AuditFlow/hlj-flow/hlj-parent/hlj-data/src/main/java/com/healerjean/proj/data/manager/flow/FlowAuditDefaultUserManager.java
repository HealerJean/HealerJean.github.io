/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.manager.flow;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.flow.FlowAuditDefaultUserDao;
import com.healerjean.proj.data.pojo.flow.FlowAuditDefaultUser;
import com.healerjean.proj.data.pojo.flow.FlowAuditDefaultUserPage;
import com.healerjean.proj.data.pojo.flow.FlowAuditDefaultUserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowAuditDefaultUserManager
 * @date 2099/1/1
 * @Description: FlowAuditDefaultUserManager
 */
@Component("flowAuditDefaultUserManager")
public class FlowAuditDefaultUserManager {

	@Autowired
	@Qualifier("flowAuditDefaultUserDao")
	private FlowAuditDefaultUserDao flowAuditDefaultUserDao;

	public FlowAuditDefaultUserDao getDao() {
		return this.flowAuditDefaultUserDao;
	}

	public int save(FlowAuditDefaultUser flowAuditDefaultUser) {
		int cnt = 0;
		if (flowAuditDefaultUser.getId() == null) {
			cnt = flowAuditDefaultUserDao.insertSelective(flowAuditDefaultUser);
		} else {
			cnt = flowAuditDefaultUserDao.updateByPrimaryKeySelective(flowAuditDefaultUser);
		}
		return cnt;
	}

	public int update(FlowAuditDefaultUser flowAuditDefaultUser) {
		return flowAuditDefaultUserDao.updateByPrimaryKey(flowAuditDefaultUser);
	}

	public int updateSelective(FlowAuditDefaultUser flowAuditDefaultUser) {
		return flowAuditDefaultUserDao.updateByPrimaryKeySelective(flowAuditDefaultUser);
	}

	public int insert(FlowAuditDefaultUser flowAuditDefaultUser) {
		return flowAuditDefaultUserDao.insert(flowAuditDefaultUser);
	}

	public int insertSelective(FlowAuditDefaultUser flowAuditDefaultUser) {
		return flowAuditDefaultUserDao.insertSelective(flowAuditDefaultUser);
	}

	public int batchInsert(List<FlowAuditDefaultUser> list){
		return flowAuditDefaultUserDao.batchInsert(list);
	}

	public FlowAuditDefaultUser findById(long id) {
		return flowAuditDefaultUserDao.selectByPrimaryKey(id);
	}

	public FlowAuditDefaultUser findByQueryContion(FlowAuditDefaultUserQuery query) {
		List<FlowAuditDefaultUser> list = flowAuditDefaultUserDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<FlowAuditDefaultUser> queryList(FlowAuditDefaultUserQuery query) {
		return flowAuditDefaultUserDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return flowAuditDefaultUserDao.deleteByPrimaryKey(id);
	}

	public int delete(FlowAuditDefaultUserQuery query) {
		return flowAuditDefaultUserDao.deleteByExample(query);
	}

	public FlowAuditDefaultUserPage queryPageList(FlowAuditDefaultUserQuery query) {
		FlowAuditDefaultUserPage flowAuditDefaultUserPage = new FlowAuditDefaultUserPage();
		Integer itemCount = flowAuditDefaultUserDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			flowAuditDefaultUserPage.setValues(null);
		} else {
			flowAuditDefaultUserPage.setValues(flowAuditDefaultUserDao.selectPageByExample(query));
		}

		flowAuditDefaultUserPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return flowAuditDefaultUserPage;
	}

}
