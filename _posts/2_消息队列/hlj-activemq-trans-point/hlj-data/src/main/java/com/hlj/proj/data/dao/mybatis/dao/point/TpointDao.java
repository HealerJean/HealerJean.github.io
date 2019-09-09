/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.dao.mybatis.dao.point;

import java.util.List;

import com.hlj.proj.data.dao.mybatis.dao.BaseDao;
import com.hlj.proj.data.pojo.user.Tpoint;
import com.hlj.proj.data.pojo.user.TpointQuery;
import org.springframework.stereotype.Repository;

/**
 * @author zhangyujin
 * @ClassName: TpointDao
 * @date 2099/1/1
 * @Description: TpointDao
 */
@Repository("tpointDao")
public class TpointDao extends BaseDao {

	public int countByExample(TpointQuery example) {
		return super.getSqlSession().selectOne("TpointMapper.countByExample", example);
	}

	public int deleteByExample(TpointQuery example) {
		return super.getSqlSession().delete("TpointMapper.deleteByExample", example);
	}

	public int deleteByPrimaryKey(long id) {
		return super.getSqlSession().delete("TpointMapper.deleteByPrimaryKey", id);
	}

	public int insert(Tpoint record) {
		return super.getSqlSession().insert("TpointMapper.insert", record);
	}

	public int insertSelective(Tpoint record) {
		return super.getSqlSession().insert("TpointMapper.insertSelective", record);
	}

	public int batchInsert(List<Tpoint> list) {
		return super.batchInsert("TpointMapper.insertSelective", list);
	}

	public List<Tpoint> selectByExample(TpointQuery example) {
		return super.getSqlSession().selectList("TpointMapper.selectByExample", example);
	}

	public List<Tpoint> selectPageByExample(TpointQuery example) {
		return super.getSqlSession().selectList("TpointMapper.selectPageByExample", example);
	}

	public Tpoint selectByPrimaryKey(long id) {
		return super.getSqlSession().selectOne("TpointMapper.selectByPrimaryKey", id);
	}

	public int updateByPrimaryKeySelective(Tpoint record) {
		return super.getSqlSession().update("TpointMapper.updateByPrimaryKeySelective", record);
	}

	public int updateByPrimaryKey(Tpoint record) {
		return super.getSqlSession().update("TpointMapper.updateByPrimaryKey", record);
	}

}
