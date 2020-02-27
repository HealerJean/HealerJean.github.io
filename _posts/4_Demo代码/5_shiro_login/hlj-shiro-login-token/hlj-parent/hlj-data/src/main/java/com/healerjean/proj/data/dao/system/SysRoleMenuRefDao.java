/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.system;

import java.util.List;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.system.SysRoleMenuRef;
import com.healerjean.proj.data.pojo.system.SysRoleMenuRefQuery;
import org.springframework.stereotype.Repository;

/**
 * @author zhangyujin
 * @ClassName: SysRoleMenuRefDao
 * @date 2099/1/1
 * @Description: SysRoleMenuRefDao
 */
@Repository("sysRoleMenuRefDao")
public class SysRoleMenuRefDao extends BaseDao {

	public int countByExample(SysRoleMenuRefQuery example) {
		return super.getSqlSession().selectOne("SysRoleMenuRefMapper.countByExample", example);
	}

	public int deleteByExample(SysRoleMenuRefQuery example) {
		return super.getSqlSession().delete("SysRoleMenuRefMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("SysRoleMenuRefMapper.deleteByPrimaryKey", id);
	}

	public int insert(SysRoleMenuRef record) {
		return super.getSqlSession().insert("SysRoleMenuRefMapper.insert", record);
	}

	public int insertSelective(SysRoleMenuRef record) {
		return super.getSqlSession().insert("SysRoleMenuRefMapper.insertSelective", record);
	}

	public int batchInsert(List<SysRoleMenuRef> list) {
		return super.batchInsert("SysRoleMenuRefMapper.insertSelective", list);
	}

	public List<SysRoleMenuRef> selectByExample(SysRoleMenuRefQuery example) {
		return super.getSqlSession().selectList("SysRoleMenuRefMapper.selectByExample", example);
	}

	public List<SysRoleMenuRef> selectPageByExample(SysRoleMenuRefQuery example) {
		return super.getSqlSession().selectList("SysRoleMenuRefMapper.selectPageByExample", example);
	}

	public SysRoleMenuRef selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("SysRoleMenuRefMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(SysRoleMenuRef record) {
		return super.getSqlSession().update("SysRoleMenuRefMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(SysRoleMenuRef record) {
		return super.getSqlSession().update("SysRoleMenuRefMapper.updateByPrimaryKey", record);
	}

}
