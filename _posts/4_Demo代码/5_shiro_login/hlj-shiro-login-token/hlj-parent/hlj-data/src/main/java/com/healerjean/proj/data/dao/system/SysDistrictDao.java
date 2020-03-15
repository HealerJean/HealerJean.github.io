/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.dao.system;

import java.util.List;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.system.SysDistrict;
import com.healerjean.proj.data.pojo.system.SysDistrictQuery;
import org.springframework.stereotype.Repository;

/**
 * @author zhangyujin
 * @ClassName: SysDistrictDao
 * @date 2099/1/1
 * @Description: SysDistrictDao
 */
@Repository("sysDistrictDao")
public class SysDistrictDao extends BaseDao {

    public int countByExample(SysDistrictQuery example) {
        return super.getSqlSession().selectOne("SysDistrictMapper.countByExample", example);
    }

    public int deleteByExample(SysDistrictQuery example) {
        return super.getSqlSession().delete("SysDistrictMapper.deleteByExample", example);
    }

    public int deleteByPrimaryKey(long id) {
        return super.getSqlSession().delete("SysDistrictMapper.deleteByPrimaryKey", id);
    }

    public int insert(SysDistrict record) {
        return super.getSqlSession().insert("SysDistrictMapper.insert", record);
    }

    public int insertSelective(SysDistrict record) {
        return super.getSqlSession().insert("SysDistrictMapper.insertSelective", record);
    }

    public int batchInsert(List<SysDistrict> list) {
        return super.batchInsert("SysDistrictMapper.insertSelective", list);
    }

    public List<SysDistrict> selectByExample(SysDistrictQuery example) {
        return super.getSqlSession().selectList("SysDistrictMapper.selectByExample", example);
    }

    public List<SysDistrict> selectPageByExample(SysDistrictQuery example) {
        return super.getSqlSession().selectList("SysDistrictMapper.selectPageByExample", example);
    }

    public SysDistrict selectByPrimaryKey(long id) {
        return super.getSqlSession().selectOne("SysDistrictMapper.selectByPrimaryKey", id);
    }

    public int updateByPrimaryKeySelective(SysDistrict record) {
        return super.getSqlSession().update("SysDistrictMapper.updateByPrimaryKeySelective", record);
    }

    public int updateByPrimaryKey(SysDistrict record) {
        return super.getSqlSession().update("SysDistrictMapper.updateByPrimaryKey", record);
    }

}
