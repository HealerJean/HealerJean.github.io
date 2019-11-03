/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.dao.system;

import com.healerjean.proj.data.dao.BaseDao;
import com.healerjean.proj.data.pojo.system.SysMenu;
import com.healerjean.proj.data.pojo.system.SysMenuQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: SysMenuDao
 * @date 2099/1/1
 * @Description: SysMenuDao
 */
@Repository("sysMenuDao")
public class SysMenuDao extends BaseDao {

    public int countByExample(SysMenuQuery example) {
        return super.getSqlSession().selectOne("SysMenuMapper.countByExample", example);
    }

    public int deleteByExample(SysMenuQuery example) {
        return super.getSqlSession().delete("SysMenuMapper.deleteByExample", example);
    }

    public int deleteByPrimaryKey(long id) {
        return super.getSqlSession().delete("SysMenuMapper.deleteByPrimaryKey", id);
    }

    public int insert(SysMenu record) {
        return super.getSqlSession().insert("SysMenuMapper.insert", record);
    }

    public int insertSelective(SysMenu record) {
        return super.getSqlSession().insert("SysMenuMapper.insertSelective", record);
    }

    public int batchInsert(List<SysMenu> list) {
        return super.batchInsert("SysMenuMapper.insertSelective", list);
    }

    public List<SysMenu> selectByExample(SysMenuQuery example) {
        return super.getSqlSession().selectList("SysMenuMapper.selectByExample", example);
    }

    public List<SysMenu> selectPageByExample(SysMenuQuery example) {
        return super.getSqlSession().selectList("SysMenuMapper.selectPageByExample", example);
    }

    public SysMenu selectByPrimaryKey(long id) {
        return super.getSqlSession().selectOne("SysMenuMapper.selectByPrimaryKey", id);
    }

    public int updateByPrimaryKeySelective(SysMenu record) {
        return super.getSqlSession().update("SysMenuMapper.updateByPrimaryKeySelective", record);
    }

    public int updateByPrimaryKey(SysMenu record) {
        return super.getSqlSession().update("SysMenuMapper.updateByPrimaryKey", record);
    }




    public List<SysMenu> selectPageByExampleLike(SysMenuQuery example) {
        return super.getSqlSession().selectList("SysMenuMapper.selectPageByExampleLike", example);
    }

    public int countByExampleLike(SysMenuQuery example) {
        return super.getSqlSession().selectOne("SysMenuMapper.countByExampleLike", example);
    }


    public List<SysMenu> selectByExampleToMenu(SysMenuQuery example) {
        return super.getSqlSession().selectList("SysMenuMapper.selectByExampleToMenu", example);
    }

    public List<SysMenu> selectMenusByRoleId(SysMenuQuery example) {
        return super.getSqlSession().selectList("SysMenuMapper.selectMenusByRoleId", example);
    }



    public List<SysMenu> selectByPrimaryKeys(List<Long> ids) {
        return super.getSqlSession().selectList("SysMenuMapper.selectByPrimaryKeys", ids);
    }

    public int batchUpdate(List<SysMenu> list) {
        return super.batchUpdate("SysMenuMapper.updateByPrimaryKeySelective", list);
    }



}
