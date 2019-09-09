/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.dao.mybatis.manager.point;

import java.util.List;

import com.hlj.proj.data.common.paging.Pagenation;
import com.hlj.proj.data.dao.mybatis.dao.point.TpointDao;
import com.hlj.proj.data.pojo.user.Tpoint;
import com.hlj.proj.data.pojo.user.TpointPage;
import com.hlj.proj.data.pojo.user.TpointQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author zhangyujin
 * @ClassName: TpointManager
 * @date 2099/1/1
 * @Description: TpointManager
 */
@Component("tpointManager")
public class TpointManager {

	@Autowired
	@Qualifier("tpointDao")
	private TpointDao tpointDao;

	public TpointDao getDao() {
		return this.tpointDao;
	}

	public int save(Tpoint tpoint) {
		int cnt = 0;
		if (tpoint.getId() == null) {
			cnt = tpointDao.insertSelective(tpoint);
		} else {
			cnt = tpointDao.updateByPrimaryKeySelective(tpoint);
		}
		return cnt;
	}

	public int update(Tpoint tpoint) {
		return tpointDao.updateByPrimaryKey(tpoint);
	}

	public int updateSelective(Tpoint tpoint) {
		return tpointDao.updateByPrimaryKeySelective(tpoint);
	}

	public int insert(Tpoint tpoint) {
		return tpointDao.insert(tpoint);
	}

	public int insertSelective(Tpoint tpoint) {
		return tpointDao.insertSelective(tpoint);
	}

	public int batchInsert(List<Tpoint> list){
		return tpointDao.batchInsert(list);
	}

	public Tpoint findById(long id) {
		return tpointDao.selectByPrimaryKey(id);
	}

	public Tpoint findByQueryContion(TpointQuery query) {
		List<Tpoint> list = tpointDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<Tpoint> queryList(TpointQuery query) {
		return tpointDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return tpointDao.deleteByPrimaryKey(id);
	}

	public int delete(TpointQuery query) {
		return tpointDao.deleteByExample(query);
	}

	public TpointPage queryPageList(TpointQuery query) {
		TpointPage tpointPage = new TpointPage();
		Integer itemCount = tpointDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			tpointPage.setValues(null);
		} else {
			tpointPage.setValues(tpointDao.selectPageByExample(query));
		}

		tpointPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return tpointPage;
	}

}
