/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.manager.system;

import java.util.List;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.system.SysUserDepartmentRefDao;
import com.healerjean.proj.data.pojo.system.SysDepartment;
import com.healerjean.proj.data.pojo.system.SysUserDepartmentRef;
import com.healerjean.proj.data.pojo.system.SysUserDepartmentRefPage;
import com.healerjean.proj.data.pojo.system.SysUserDepartmentRefQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author zhangyujin
 * @ClassName: SysUserDepartmentRefManager
 * @date 2099/1/1
 * @Description: SysUserDepartmentRefManager
 */
@Component("sysUserDepartmentRefManager")
public class SysUserDepartmentRefManager {

	@Autowired
	@Qualifier("sysUserDepartmentRefDao")
	private SysUserDepartmentRefDao sysUserDepartmentRefDao;

	public SysUserDepartmentRefDao getDao() {
		return this.sysUserDepartmentRefDao;
	}

	public int save(SysUserDepartmentRef sysUserDepartmentRef) {
		int cnt = 0;
		if (sysUserDepartmentRef.getId() == null) {
			cnt = sysUserDepartmentRefDao.insertSelective(sysUserDepartmentRef);
		} else {
			cnt = sysUserDepartmentRefDao.updateByPrimaryKeySelective(sysUserDepartmentRef);
		}
		return cnt;
	}

	public int update(SysUserDepartmentRef sysUserDepartmentRef) {
		return sysUserDepartmentRefDao.updateByPrimaryKey(sysUserDepartmentRef);
	}

	public int updateSelective(SysUserDepartmentRef sysUserDepartmentRef) {
		return sysUserDepartmentRefDao.updateByPrimaryKeySelective(sysUserDepartmentRef);
	}

	public int insert(SysUserDepartmentRef sysUserDepartmentRef) {
		return sysUserDepartmentRefDao.insert(sysUserDepartmentRef);
	}

	public int insertSelective(SysUserDepartmentRef sysUserDepartmentRef) {
		return sysUserDepartmentRefDao.insertSelective(sysUserDepartmentRef);
	}

	public int batchInsert(List<SysUserDepartmentRef> list){
		return sysUserDepartmentRefDao.batchInsert(list);
	}

	public SysUserDepartmentRef findById(long id) {
		return sysUserDepartmentRefDao.selectByPrimaryKey(id);
	}

	public SysUserDepartmentRef findByQueryContion(SysUserDepartmentRefQuery query) {
		List<SysUserDepartmentRef> list = sysUserDepartmentRefDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<SysUserDepartmentRef> queryList(SysUserDepartmentRefQuery query) {
		return sysUserDepartmentRefDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return sysUserDepartmentRefDao.deleteByPrimaryKey(id);
	}

	public int delete(SysUserDepartmentRefQuery query) {
		return sysUserDepartmentRefDao.deleteByExample(query);
	}

	public SysUserDepartmentRefPage queryPageList(SysUserDepartmentRefQuery query) {
		SysUserDepartmentRefPage sysUserDepartmentRefPage = new SysUserDepartmentRefPage();
		Integer itemCount = sysUserDepartmentRefDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			sysUserDepartmentRefPage.setValues(null);
		} else {
			sysUserDepartmentRefPage.setValues(sysUserDepartmentRefDao.selectPageByExample(query));
		}

		sysUserDepartmentRefPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return sysUserDepartmentRefPage;
	}


	public SysDepartment findByQueryContionToDepartment(SysUserDepartmentRefQuery query) {
		SysDepartment SysDepartment = sysUserDepartmentRefDao.selectByExampleToUser(query);
		return SysDepartment;
	}

}
