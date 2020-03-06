/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.dao.system;

import java.util.List;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.system.SysDictionaryType;
import com.healerjean.proj.data.pojo.system.SysDictionaryTypeQuery;
import org.springframework.stereotype.Repository;

/**
 * @author zhangyujin
 * @ClassName: SysDictionaryTypeDao
 * @date 2099/1/1
 * @Description: SysDictionaryTypeDao
 */
@Repository("sysDictionaryTypeDao")
public class SysDictionaryTypeDao extends BaseDao {

    public int countByExample(SysDictionaryTypeQuery example) {
        return super.getSqlSession().selectOne("SysDictionaryTypeMapper.countByExample", example);
    }

    public int deleteByExample(SysDictionaryTypeQuery example) {
        return super.getSqlSession().delete("SysDictionaryTypeMapper.deleteByExample", example);
    }

    public int deleteByPrimaryKey(long id) {
        return super.getSqlSession().delete("SysDictionaryTypeMapper.deleteByPrimaryKey", id);
    }

    public int insert(SysDictionaryType record) {
        return super.getSqlSession().insert("SysDictionaryTypeMapper.insert", record);
    }

    public int insertSelective(SysDictionaryType record) {
        return super.getSqlSession().insert("SysDictionaryTypeMapper.insertSelective", record);
    }

    public int batchInsert(List<SysDictionaryType> list) {
        return super.batchInsert("SysDictionaryTypeMapper.insertSelective", list);
    }

    public List<SysDictionaryType> selectByExample(SysDictionaryTypeQuery example) {
        return super.getSqlSession().selectList("SysDictionaryTypeMapper.selectByExample", example);
    }

    public List<SysDictionaryType> selectPageByExample(SysDictionaryTypeQuery example) {
        return super.getSqlSession().selectList("SysDictionaryTypeMapper.selectPageByExample", example);
    }

    public SysDictionaryType selectByPrimaryKey(long id) {
        return super.getSqlSession().selectOne("SysDictionaryTypeMapper.selectByPrimaryKey", id);
    }

    public int updateByPrimaryKeySelective(SysDictionaryType record) {
        return super.getSqlSession().update("SysDictionaryTypeMapper.updateByPrimaryKeySelective", record);
    }

    public int updateByPrimaryKey(SysDictionaryType record) {
        return super.getSqlSession().update("SysDictionaryTypeMapper.updateByPrimaryKey", record);
    }


    public Integer countDictTypeLikes(SysDictionaryTypeQuery query) {
        return super.getSqlSession().selectOne("SysDictionaryTypeMapper.countDictTypeLikes", query);
    }

    public List<SysDictionaryType> queryDictTypePageLikes(SysDictionaryTypeQuery query) {
        return super.getSqlSession().selectList("SysDictionaryTypeMapper.queryDictTypePageLikes", query);
    }

    public List<SysDictionaryType> queryDictTypeLikes(SysDictionaryTypeQuery query) {
        return super.getSqlSession().selectList("SysDictionaryTypeMapper.queryDictTypeLikes", query);
    }
}
