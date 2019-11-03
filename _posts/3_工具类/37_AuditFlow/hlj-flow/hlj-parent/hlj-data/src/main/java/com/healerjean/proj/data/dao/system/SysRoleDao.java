/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.system;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.system.SysRole;
import com.healerjean.proj.data.pojo.system.SysRoleQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: SysRoleDao
 * @date 2099/1/1
 * @Description: SysRoleDao
 */
@Repository("sysRoleDao")
public class SysRoleDao extends BaseDao {

	public int countByExample(SysRoleQuery example) {
		return super.getSqlSession().selectOne("SysRoleMapper.countByExample", example);
	}

	public int deleteByExample(SysRoleQuery example) {
		return super.getSqlSession().delete("SysRoleMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("SysRoleMapper.deleteByPrimaryKey", id);
	}

	public int insert(SysRole record) {
		return super.getSqlSession().insert("SysRoleMapper.insert", record);
	}

	public int insertSelective(SysRole record) {
		return super.getSqlSession().insert("SysRoleMapper.insertSelective", record);
	}

	public int batchInsert(List<SysRole> list) {
		return super.batchInsert("SysRoleMapper.insertSelective", list);
	}

	public List<SysRole> selectByExample(SysRoleQuery example) {
		return super.getSqlSession().selectList("SysRoleMapper.selectByExample", example);
	}

	public List<SysRole> selectPageByExample(SysRoleQuery example) {
		return super.getSqlSession().selectList("SysRoleMapper.selectPageByExample", example);
	}

	public SysRole selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("SysRoleMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(SysRole record) {
		return super.getSqlSession().update("SysRoleMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(SysRole record) {
		return super.getSqlSession().update("SysRoleMapper.updateByPrimaryKey", record);
	}







	public List<SysRole> selectByExampleLike(SysRoleQuery example) {
		return super.getSqlSession().selectList("SysRoleMapper.selectByExampleLike", example);
	}


	public int countByExampleLike(SysRoleQuery example) {
		return super.getSqlSession().selectOne("SysRoleMapper.countByExampleLike", example);
	}

	public List<SysRole> selectPageByExampleLike(SysRoleQuery example) {
		return super.getSqlSession().selectList("SysRoleMapper.selectPageByExampleLike", example);
	}

}
