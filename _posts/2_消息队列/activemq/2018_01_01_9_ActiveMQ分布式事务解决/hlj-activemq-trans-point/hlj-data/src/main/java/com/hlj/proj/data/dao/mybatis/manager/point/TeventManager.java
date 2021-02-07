/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.dao.mybatis.manager.point;

import java.util.List;

import com.hlj.proj.data.common.paging.Pagenation;
import com.hlj.proj.data.dao.mybatis.dao.point.TeventDao;
import com.hlj.proj.data.pojo.user.Tevent;
import com.hlj.proj.data.pojo.user.TeventPage;
import com.hlj.proj.data.pojo.user.TeventQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
/**
 * @author zhangyujin
 * @ClassName: TeventManager
 * @date 2099/1/1
 * @Description: TeventManager
 */
@Component("teventManager")
public class TeventManager {

	@Autowired
	@Qualifier("teventDao")
	private TeventDao teventDao;

	public TeventDao getDao() {
		return this.teventDao;
	}

	public int save(Tevent tevent) {
		int cnt = 0;
		if (tevent.getId() == null) {
			cnt = teventDao.insertSelective(tevent);
		} else {
			cnt = teventDao.updateByPrimaryKeySelective(tevent);
		}
		return cnt;
	}

	public int update(Tevent tevent) {
		return teventDao.updateByPrimaryKey(tevent);
	}

	public int updateSelective(Tevent tevent) {
		return teventDao.updateByPrimaryKeySelective(tevent);
	}

	public int insert(Tevent tevent) {
		return teventDao.insert(tevent);
	}

	public int insertSelective(Tevent tevent) {
		return teventDao.insertSelective(tevent);
	}

	public int batchInsert(List<Tevent> list){
		return teventDao.batchInsert(list);
	}

	public Tevent findById(long id) {
		return teventDao.selectByPrimaryKey(id);
	}

	public Tevent findByQueryContion(TeventQuery query) {
		List<Tevent> list = teventDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<Tevent> queryList(TeventQuery query) {
		return teventDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return teventDao.deleteByPrimaryKey(id);
	}

	public int delete(TeventQuery query) {
		return teventDao.deleteByExample(query);
	}

	public TeventPage queryPageList(TeventQuery query) {
		TeventPage teventPage = new TeventPage();
		Integer itemCount = teventDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			teventPage.setValues(null);
		} else {
			teventPage.setValues(teventDao.selectPageByExample(query));
		}

		teventPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return teventPage;
	}

}
