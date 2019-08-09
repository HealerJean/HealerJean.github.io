/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.dao.mybatis.manager.flow;

import com.hlj.proj.data.common.paging.Pagenation;
import com.hlj.proj.data.dao.mybatis.dao.flow.ScfFlowAuditRecordTempDao;
import com.hlj.proj.data.pojo.flow.ScfFlowAuditRecordTemp;
import com.hlj.proj.data.pojo.flow.ScfFlowAuditRecordTempPage;
import com.hlj.proj.data.pojo.flow.ScfFlowAuditRecordTempQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author duyang
 * @ClassName: ScfFlowAuditRecordTempManager
 * @date 2099/1/1
 * @Description: ScfFlowAuditRecordTempManager
 */
@Component("scfFlowAuditRecordTempManager")
public class ScfFlowAuditRecordTempManager {

	@Autowired
	@Qualifier("scfFlowAuditRecordTempDao")
	private ScfFlowAuditRecordTempDao scfFlowAuditRecordTempDao;

	public ScfFlowAuditRecordTempDao getDao() {
		return this.scfFlowAuditRecordTempDao;
	}

	public int save(ScfFlowAuditRecordTemp scfFlowAuditRecordTemp) {
		int cnt = 0;
		if (scfFlowAuditRecordTemp.getId() == null) {
			cnt = scfFlowAuditRecordTempDao.insertSelective(scfFlowAuditRecordTemp);
		} else {
			cnt = scfFlowAuditRecordTempDao.updateByPrimaryKeySelective(scfFlowAuditRecordTemp);
		}
		return cnt;
	}

	public int update(ScfFlowAuditRecordTemp scfFlowAuditRecordTemp) {
		return scfFlowAuditRecordTempDao.updateByPrimaryKey(scfFlowAuditRecordTemp);
	}

	public int updateSelective(ScfFlowAuditRecordTemp scfFlowAuditRecordTemp) {
		return scfFlowAuditRecordTempDao.updateByPrimaryKeySelective(scfFlowAuditRecordTemp);
	}

	public int insert(ScfFlowAuditRecordTemp scfFlowAuditRecordTemp) {
		return scfFlowAuditRecordTempDao.insert(scfFlowAuditRecordTemp);
	}

	public int insertSelective(ScfFlowAuditRecordTemp scfFlowAuditRecordTemp) {
		return scfFlowAuditRecordTempDao.insertSelective(scfFlowAuditRecordTemp);
	}

	public int batchInsert(List<ScfFlowAuditRecordTemp> list){
		return scfFlowAuditRecordTempDao.batchInsert(list);
	}

	public ScfFlowAuditRecordTemp findById(long id) {
		return scfFlowAuditRecordTempDao.selectByPrimaryKey(id);
	}

	public ScfFlowAuditRecordTemp findByQueryContion(ScfFlowAuditRecordTempQuery query) {
		List<ScfFlowAuditRecordTemp> list = scfFlowAuditRecordTempDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<ScfFlowAuditRecordTemp> queryList(ScfFlowAuditRecordTempQuery query) {
		return scfFlowAuditRecordTempDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return scfFlowAuditRecordTempDao.deleteByPrimaryKey(id);
	}

	public int delete(ScfFlowAuditRecordTempQuery query) {
		return scfFlowAuditRecordTempDao.deleteByExample(query);
	}

	public ScfFlowAuditRecordTempPage queryPageList(ScfFlowAuditRecordTempQuery query) {
		ScfFlowAuditRecordTempPage scfFlowAuditRecordTempPage = new ScfFlowAuditRecordTempPage();
		Integer itemCount = scfFlowAuditRecordTempDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			scfFlowAuditRecordTempPage.setValues(null);
		} else {
			scfFlowAuditRecordTempPage.setValues(scfFlowAuditRecordTempDao.selectPageByExample(query));
		}

		scfFlowAuditRecordTempPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return scfFlowAuditRecordTempPage;
	}

	public List<ScfFlowAuditRecordTemp> queryListGroupByFlowCode(ScfFlowAuditRecordTempQuery query) {
		return scfFlowAuditRecordTempDao.queryListGroupByFlowCode(query);
	}

	public ScfFlowAuditRecordTempPage queryListPageGroupByByFlowCode(ScfFlowAuditRecordTempQuery query) {
		ScfFlowAuditRecordTempPage scfFlowAuditRecordTempPage = new ScfFlowAuditRecordTempPage();
		Integer itemCount = scfFlowAuditRecordTempDao.queryListPageCount(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			scfFlowAuditRecordTempPage.setValues(null);
		} else {
			scfFlowAuditRecordTempPage.setValues(scfFlowAuditRecordTempDao.queryListPage(query));
		}

		scfFlowAuditRecordTempPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return scfFlowAuditRecordTempPage;
	}
}
