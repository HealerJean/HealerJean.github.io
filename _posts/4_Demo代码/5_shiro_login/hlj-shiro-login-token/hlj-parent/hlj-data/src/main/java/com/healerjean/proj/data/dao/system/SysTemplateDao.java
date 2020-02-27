/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.system;

import java.util.List;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.system.SysTemplate;
import com.healerjean.proj.data.pojo.system.SysTemplateQuery;
import org.springframework.stereotype.Repository;

/**
 * @author zhangyujin
 * @ClassName: SysTemplateDao
 * @date 2099/1/1
 * @Description: SysTemplateDao
 */
@Repository("sysTemplateDao")
public class SysTemplateDao extends BaseDao {

	public int countByExample(SysTemplateQuery example) {
		return super.getSqlSession().selectOne("SysTemplateMapper.countByExample", example);
	}

	public int deleteByExample(SysTemplateQuery example) {
		return super.getSqlSession().delete("SysTemplateMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("SysTemplateMapper.deleteByPrimaryKey", id);
	}

	public int insert(SysTemplate record) {
		return super.getSqlSession().insert("SysTemplateMapper.insert", record);
	}

	public int insertSelective(SysTemplate record) {
		return super.getSqlSession().insert("SysTemplateMapper.insertSelective", record);
	}

	public int batchInsert(List<SysTemplate> list) {
		return super.batchInsert("SysTemplateMapper.insertSelective", list);
	}

	public List<SysTemplate> selectByExample(SysTemplateQuery example) {
		return super.getSqlSession().selectList("SysTemplateMapper.selectByExample", example);
	}

	public List<SysTemplate> selectPageByExample(SysTemplateQuery example) {
		return super.getSqlSession().selectList("SysTemplateMapper.selectPageByExample", example);
	}

	public SysTemplate selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("SysTemplateMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(SysTemplate record) {
		return super.getSqlSession().update("SysTemplateMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(SysTemplate record) {
		return super.getSqlSession().update("SysTemplateMapper.updateByPrimaryKey", record);
	}

}
