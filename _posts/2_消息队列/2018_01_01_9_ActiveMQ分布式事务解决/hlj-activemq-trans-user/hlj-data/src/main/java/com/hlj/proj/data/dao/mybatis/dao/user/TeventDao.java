/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.dao.mybatis.dao.user;

import java.util.List;

import com.hlj.proj.data.pojo.user.Tevent;
import com.hlj.proj.data.pojo.user.TeventQuery;
import com.hlj.proj.data.dao.mybatis.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * @author zhangyujin
 * @ClassName: TeventDao
 * @date 2099/1/1
 * @Description: TeventDao
 */
@Repository("teventDao")
public class TeventDao extends BaseDao {

	public int countByExample(TeventQuery example) {
		return super.getSqlSession().selectOne("TeventMapper.countByExample", example);
	}

	public int deleteByExample(TeventQuery example) {
		return super.getSqlSession().delete("TeventMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("TeventMapper.deleteByPrimaryKey", id);
	}

	public int insert(Tevent record) {
		return super.getSqlSession().insert("TeventMapper.insert", record);
	}

	public int insertSelective(Tevent record) {
		return super.getSqlSession().insert("TeventMapper.insertSelective", record);
	}

	public int batchInsert(List<Tevent> list) {
		return super.batchInsert("TeventMapper.insertSelective", list);
	}

	public List<Tevent> selectByExample(TeventQuery example) {
		return super.getSqlSession().selectList("TeventMapper.selectByExample", example);
	}

	public List<Tevent> selectPageByExample(TeventQuery example) {
		return super.getSqlSession().selectList("TeventMapper.selectPageByExample", example);
	}

	public Tevent selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("TeventMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(Tevent record) {
		return super.getSqlSession().update("TeventMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(Tevent record) {
		return super.getSqlSession().update("TeventMapper.updateByPrimaryKey", record);
	}

}
