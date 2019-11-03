/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.manager.flow;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.flow.FlowWorkRecordDao;
import com.healerjean.proj.data.pojo.flow.FlowWorkRecord;
import com.healerjean.proj.data.pojo.flow.FlowWorkRecordPage;
import com.healerjean.proj.data.pojo.flow.FlowWorkRecordQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowWorkRecordManager
 * @date 2099/1/1
 * @Description: FlowWorkRecordManager
 */
@Component("flowWorkRecordManager")
public class FlowWorkRecordManager {

	@Autowired
	@Qualifier("flowWorkRecordDao")
	private FlowWorkRecordDao flowWorkRecordDao;

	public FlowWorkRecordDao getDao() {
		return this.flowWorkRecordDao;
	}

	public int save(FlowWorkRecord flowWorkRecord) {
		int cnt = 0;
		if (flowWorkRecord.getId() == null) {
			cnt = flowWorkRecordDao.insertSelective(flowWorkRecord);
		} else {
			cnt = flowWorkRecordDao.updateByPrimaryKeySelective(flowWorkRecord);
		}
		return cnt;
	}

	public int update(FlowWorkRecord flowWorkRecord) {
		return flowWorkRecordDao.updateByPrimaryKey(flowWorkRecord);
	}

	public int updateSelective(FlowWorkRecord flowWorkRecord) {
		return flowWorkRecordDao.updateByPrimaryKeySelective(flowWorkRecord);
	}

	public int insert(FlowWorkRecord flowWorkRecord) {
		return flowWorkRecordDao.insert(flowWorkRecord);
	}

	public int insertSelective(FlowWorkRecord flowWorkRecord) {
		return flowWorkRecordDao.insertSelective(flowWorkRecord);
	}

	public int batchInsert(List<FlowWorkRecord> list){
		return flowWorkRecordDao.batchInsert(list);
	}

	public FlowWorkRecord findById(long id) {
		return flowWorkRecordDao.selectByPrimaryKey(id);
	}

	public FlowWorkRecord findByQueryContion(FlowWorkRecordQuery query) {
		List<FlowWorkRecord> list = flowWorkRecordDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<FlowWorkRecord> queryList(FlowWorkRecordQuery query) {
		return flowWorkRecordDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return flowWorkRecordDao.deleteByPrimaryKey(id);
	}

	public int delete(FlowWorkRecordQuery query) {
		return flowWorkRecordDao.deleteByExample(query);
	}

	public FlowWorkRecordPage queryPageList(FlowWorkRecordQuery query) {
		FlowWorkRecordPage flowWorkRecordPage = new FlowWorkRecordPage();
		Integer itemCount = flowWorkRecordDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			flowWorkRecordPage.setValues(null);
		} else {
			flowWorkRecordPage.setValues(flowWorkRecordDao.selectPageByExample(query));
		}

		flowWorkRecordPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return flowWorkRecordPage;
	}

}
