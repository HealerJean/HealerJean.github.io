/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.dao.mybatis.dao.user;

import java.util.List;

import com.hlj.proj.data.pojo.user.Tuser;
import com.hlj.proj.data.pojo.user.TuserQuery;
import com.hlj.proj.data.dao.mybatis.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * @author zhangyujin
 * @ClassName: TuserDao
 * @date 2099/1/1
 * @Description: TuserDao
 */
@Repository("tuserDao")
public class TuserDao extends BaseDao {

	public int countByExample(TuserQuery example) {
		return super.getSqlSession().selectOne("TuserMapper.countByExample", example);
	}

	public int deleteByExample(TuserQuery example) {
		return super.getSqlSession().delete("TuserMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("TuserMapper.deleteByPrimaryKey", id);
	}

	public int insert(Tuser record) {
		return super.getSqlSession().insert("TuserMapper.insert", record);
	}

	public int insertSelective(Tuser record) {
		return super.getSqlSession().insert("TuserMapper.insertSelective", record);
	}

	public int batchInsert(List<Tuser> list) {
		return super.batchInsert("TuserMapper.insertSelective", list);
	}

	public List<Tuser> selectByExample(TuserQuery example) {
		return super.getSqlSession().selectList("TuserMapper.selectByExample", example);
	}

	public List<Tuser> selectPageByExample(TuserQuery example) {
		return super.getSqlSession().selectList("TuserMapper.selectPageByExample", example);
	}

	public Tuser selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("TuserMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(Tuser record) {
		return super.getSqlSession().update("TuserMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(Tuser record) {
		return super.getSqlSession().update("TuserMapper.updateByPrimaryKey", record);
	}

}
