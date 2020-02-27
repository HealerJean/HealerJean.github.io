/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.dao.system;

import java.util.List;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.system.SysDictionaryData;
import com.healerjean.proj.data.pojo.system.SysDictionaryDataQuery;
import org.springframework.stereotype.Repository;

/**
 * @author zhangyujin
 * @ClassName: SysDictionaryDataDao
 * @date 2099/1/1
 * @Description: SysDictionaryDataDao
 */
@Repository("sysDictionaryDataDao")
public class SysDictionaryDataDao extends BaseDao {

	public int countByExample(SysDictionaryDataQuery example) {
		return super.getSqlSession().selectOne("SysDictionaryDataMapper.countByExample", example);
	}

	public int deleteByExample(SysDictionaryDataQuery example) {
		return super.getSqlSession().delete("SysDictionaryDataMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("SysDictionaryDataMapper.deleteByPrimaryKey", id);
	}

	public int insert(SysDictionaryData record) {
		return super.getSqlSession().insert("SysDictionaryDataMapper.insert", record);
	}

	public int insertSelective(SysDictionaryData record) {
		return super.getSqlSession().insert("SysDictionaryDataMapper.insertSelective", record);
	}

	public int batchInsert(List<SysDictionaryData> list) {
		return super.batchInsert("SysDictionaryDataMapper.insertSelective", list);
	}

	public List<SysDictionaryData> selectByExample(SysDictionaryDataQuery example) {
		return super.getSqlSession().selectList("SysDictionaryDataMapper.selectByExample", example);
	}

	public List<SysDictionaryData> selectPageByExample(SysDictionaryDataQuery example) {
		return super.getSqlSession().selectList("SysDictionaryDataMapper.selectPageByExample", example);
	}

	public SysDictionaryData selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("SysDictionaryDataMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(SysDictionaryData record) {
		return super.getSqlSession().update("SysDictionaryDataMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(SysDictionaryData record) {
		return super.getSqlSession().update("SysDictionaryDataMapper.updateByPrimaryKey", record);
	}


	public int countDictDataLikes(SysDictionaryDataQuery example) {
		return super.getSqlSession().selectOne("SysDictionaryDataMapper.countDictDataLikes", example);
	}
	public List<SysDictionaryData> getDictDataPageLikes(SysDictionaryDataQuery example) {
		return super.getSqlSession().selectList("SysDictionaryDataMapper.getDictDataPageLikes", example);
	}
	public List<SysDictionaryData> getDictDataLikes(SysDictionaryDataQuery example) {
		return super.getSqlSession().selectList("SysDictionaryDataMapper.getDictDataLikes", example);
	}

}
