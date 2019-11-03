/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.manager.flow;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.flow.FlowAuditRecordDao;
import com.healerjean.proj.data.pojo.flow.FlowAuditRecord;
import com.healerjean.proj.data.pojo.flow.FlowAuditRecordPage;
import com.healerjean.proj.data.pojo.flow.FlowAuditRecordQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowAuditRecordManager
 * @date 2099/1/1
 * @Description: FlowAuditRecordManager
 */
@Component("flowAuditRecordManager")
public class FlowAuditRecordManager {

	@Autowired
	@Qualifier("flowAuditRecordDao")
	private FlowAuditRecordDao flowAuditRecordDao;

	public FlowAuditRecordDao getDao() {
		return this.flowAuditRecordDao;
	}

	public int save(FlowAuditRecord flowAuditRecord) {
		int cnt = 0;
		if (flowAuditRecord.getId() == null) {
			cnt = flowAuditRecordDao.insertSelective(flowAuditRecord);
		} else {
			cnt = flowAuditRecordDao.updateByPrimaryKeySelective(flowAuditRecord);
		}
		return cnt;
	}

	public int update(FlowAuditRecord flowAuditRecord) {
		return flowAuditRecordDao.updateByPrimaryKey(flowAuditRecord);
	}

	public int updateSelective(FlowAuditRecord flowAuditRecord) {
		return flowAuditRecordDao.updateByPrimaryKeySelective(flowAuditRecord);
	}

	public int insert(FlowAuditRecord flowAuditRecord) {
		return flowAuditRecordDao.insert(flowAuditRecord);
	}

	public int insertSelective(FlowAuditRecord flowAuditRecord) {
		return flowAuditRecordDao.insertSelective(flowAuditRecord);
	}

	public int batchInsert(List<FlowAuditRecord> list){
		return flowAuditRecordDao.batchInsert(list);
	}

	public FlowAuditRecord findById(long id) {
		return flowAuditRecordDao.selectByPrimaryKey(id);
	}

	public FlowAuditRecord findByQueryContion(FlowAuditRecordQuery query) {
		List<FlowAuditRecord> list = flowAuditRecordDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<FlowAuditRecord> queryList(FlowAuditRecordQuery query) {
		return flowAuditRecordDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return flowAuditRecordDao.deleteByPrimaryKey(id);
	}

	public int delete(FlowAuditRecordQuery query) {
		return flowAuditRecordDao.deleteByExample(query);
	}

	public FlowAuditRecordPage queryPageList(FlowAuditRecordQuery query) {
		FlowAuditRecordPage flowAuditRecordPage = new FlowAuditRecordPage();
		Integer itemCount = flowAuditRecordDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			flowAuditRecordPage.setValues(null);
		} else {
			flowAuditRecordPage.setValues(flowAuditRecordDao.selectPageByExample(query));
		}

		flowAuditRecordPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return flowAuditRecordPage;
	}

}
