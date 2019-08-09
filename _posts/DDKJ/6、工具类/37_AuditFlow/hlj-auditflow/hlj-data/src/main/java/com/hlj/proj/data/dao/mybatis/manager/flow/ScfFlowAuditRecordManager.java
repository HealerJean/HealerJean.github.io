/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.dao.mybatis.manager.flow;

import com.hlj.proj.data.common.paging.Pagenation;
import com.hlj.proj.data.dao.mybatis.dao.flow.ScfFlowAuditRecordDao;
import com.hlj.proj.data.pojo.flow.ScfFlowAuditRecord;
import com.hlj.proj.data.pojo.flow.ScfFlowAuditRecordPage;
import com.hlj.proj.data.pojo.flow.ScfFlowAuditRecordQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("scfFlowAuditRecordManager")
public class ScfFlowAuditRecordManager {

	@Autowired
	@Qualifier("scfFlowAuditRecordDao")
	private ScfFlowAuditRecordDao scfFlowAuditRecordDao;

	public ScfFlowAuditRecordDao getDao() {
		return this.scfFlowAuditRecordDao;
	}

	public int save(ScfFlowAuditRecord scfFlowAuditRecord) {
		int cnt = 0;
		if (scfFlowAuditRecord.getId() == null) {
			cnt = scfFlowAuditRecordDao.insertSelective(scfFlowAuditRecord);
		} else {
			cnt = scfFlowAuditRecordDao.updateByPrimaryKeySelective(scfFlowAuditRecord);
		}
		return cnt;
	}

	public int update(ScfFlowAuditRecord scfFlowAuditRecord) {
		return scfFlowAuditRecordDao.updateByPrimaryKey(scfFlowAuditRecord);
	}

	public int updateSelective(ScfFlowAuditRecord scfFlowAuditRecord) {
		return scfFlowAuditRecordDao.updateByPrimaryKeySelective(scfFlowAuditRecord);
	}

	public int insert(ScfFlowAuditRecord scfFlowAuditRecord) {
		return scfFlowAuditRecordDao.insert(scfFlowAuditRecord);
	}

	public int insertSelective(ScfFlowAuditRecord scfFlowAuditRecord) {
		return scfFlowAuditRecordDao.insertSelective(scfFlowAuditRecord);
	}

	public int batchInsert(List<ScfFlowAuditRecord> list){
		return scfFlowAuditRecordDao.batchInsert(list);
	}

	public ScfFlowAuditRecord findById(long id) {
		return scfFlowAuditRecordDao.selectByPrimaryKey(id);
	}

	public ScfFlowAuditRecord findByQueryContion(ScfFlowAuditRecordQuery query) {
		List<ScfFlowAuditRecord> list = scfFlowAuditRecordDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<ScfFlowAuditRecord> queryList(ScfFlowAuditRecordQuery query) {
		return scfFlowAuditRecordDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return scfFlowAuditRecordDao.deleteByPrimaryKey(id);
	}

	public int delete(ScfFlowAuditRecordQuery query) {
		return scfFlowAuditRecordDao.deleteByExample(query);
	}

	public ScfFlowAuditRecordPage queryPageList(ScfFlowAuditRecordQuery query) {
		ScfFlowAuditRecordPage scfFlowAuditRecordPage = new ScfFlowAuditRecordPage();
		Integer itemCount = scfFlowAuditRecordDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			scfFlowAuditRecordPage.setValues(null);
		} else {
			scfFlowAuditRecordPage.setValues(scfFlowAuditRecordDao.selectPageByExample(query));
		}

		scfFlowAuditRecordPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return scfFlowAuditRecordPage;
	}

}
