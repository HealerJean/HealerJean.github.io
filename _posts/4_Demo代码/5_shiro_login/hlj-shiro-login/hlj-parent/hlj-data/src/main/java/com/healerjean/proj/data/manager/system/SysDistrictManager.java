/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.manager.system;

import java.util.List;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.system.SysDistrictDao;
import com.healerjean.proj.data.pojo.system.SysDistrict;
import com.healerjean.proj.data.pojo.system.SysDistrictPage;
import com.healerjean.proj.data.pojo.system.SysDistrictQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author zhangyujin
 * @ClassName: SysDistrictManager
 * @date 2099/1/1
 * @Description: SysDistrictManager
 */
@Component("sysDistrictManager")
public class SysDistrictManager {

    @Autowired
    @Qualifier("sysDistrictDao")
    private SysDistrictDao sysDistrictDao;

    public SysDistrictDao getDao() {
        return this.sysDistrictDao;
    }

    public int save(SysDistrict sysDistrict) {
        int cnt = 0;
        if (sysDistrict.getId() == null) {
            cnt = sysDistrictDao.insertSelective(sysDistrict);
        } else {
            cnt = sysDistrictDao.updateByPrimaryKeySelective(sysDistrict);
        }
        return cnt;
    }

    public int update(SysDistrict sysDistrict) {
        return sysDistrictDao.updateByPrimaryKey(sysDistrict);
    }

    public int updateSelective(SysDistrict sysDistrict) {
        return sysDistrictDao.updateByPrimaryKeySelective(sysDistrict);
    }

    public int insert(SysDistrict sysDistrict) {
        return sysDistrictDao.insert(sysDistrict);
    }

    public int insertSelective(SysDistrict sysDistrict) {
        return sysDistrictDao.insertSelective(sysDistrict);
    }

    public int batchInsert(List<SysDistrict> list) {
        return sysDistrictDao.batchInsert(list);
    }

    public SysDistrict findById(long id) {
        return sysDistrictDao.selectByPrimaryKey(id);
    }

    public SysDistrict findByQueryContion(SysDistrictQuery query) {
        List<SysDistrict> list = sysDistrictDao.selectByExample(query);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public List<SysDistrict> queryList(SysDistrictQuery query) {
        return sysDistrictDao.selectByExample(query);
    }

    public int deleteById(long id) {
        return sysDistrictDao.deleteByPrimaryKey(id);
    }

    public int delete(SysDistrictQuery query) {
        return sysDistrictDao.deleteByExample(query);
    }

    public SysDistrictPage queryPageList(SysDistrictQuery query) {
        SysDistrictPage sysDistrictPage = new SysDistrictPage();
        Integer itemCount = sysDistrictDao.countByExample(query);
        query.setItemCount(itemCount);

        if (itemCount == 0) {
            sysDistrictPage.setValues(null);
        } else {
            sysDistrictPage.setValues(sysDistrictDao.selectPageByExample(query));
        }

        sysDistrictPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
        return sysDistrictPage;
    }

}
