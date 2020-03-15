/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.manager.system;

import java.util.List;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.system.SysTemplateDao;
import com.healerjean.proj.data.pojo.system.SysTemplate;
import com.healerjean.proj.data.pojo.system.SysTemplatePage;
import com.healerjean.proj.data.pojo.system.SysTemplateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author zhangyujin
 * @ClassName: SysTemplateManager
 * @date 2099/1/1
 * @Description: SysTemplateManager
 */
@Component("sysTemplateManager")
public class SysTemplateManager {

    @Autowired
    @Qualifier("sysTemplateDao")
    private SysTemplateDao sysTemplateDao;

    public SysTemplateDao getDao() {
        return this.sysTemplateDao;
    }

    public int save(SysTemplate sysTemplate) {
        int cnt = 0;
        if (sysTemplate.getId() == null) {
            cnt = sysTemplateDao.insertSelective(sysTemplate);
        } else {
            cnt = sysTemplateDao.updateByPrimaryKeySelective(sysTemplate);
        }
        return cnt;
    }

    public int update(SysTemplate sysTemplate) {
        return sysTemplateDao.updateByPrimaryKey(sysTemplate);
    }

    public int updateSelective(SysTemplate sysTemplate) {
        return sysTemplateDao.updateByPrimaryKeySelective(sysTemplate);
    }

    public int insert(SysTemplate sysTemplate) {
        return sysTemplateDao.insert(sysTemplate);
    }

    public int insertSelective(SysTemplate sysTemplate) {
        return sysTemplateDao.insertSelective(sysTemplate);
    }

    public int batchInsert(List<SysTemplate> list) {
        return sysTemplateDao.batchInsert(list);
    }

    public SysTemplate findById(long id) {
        return sysTemplateDao.selectByPrimaryKey(id);
    }

    public SysTemplate findByQueryContion(SysTemplateQuery query) {
        List<SysTemplate> list = sysTemplateDao.selectByExample(query);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public List<SysTemplate> queryList(SysTemplateQuery query) {
        return sysTemplateDao.selectByExample(query);
    }

    public int deleteById(long id) {
        return sysTemplateDao.deleteByPrimaryKey(id);
    }

    public int delete(SysTemplateQuery query) {
        return sysTemplateDao.deleteByExample(query);
    }

    public SysTemplatePage queryPageList(SysTemplateQuery query) {
        SysTemplatePage sysTemplatePage = new SysTemplatePage();
        Integer itemCount = sysTemplateDao.countByExample(query);
        query.setItemCount(itemCount);

        if (itemCount == 0) {
            sysTemplatePage.setValues(null);
        } else {
            sysTemplatePage.setValues(sysTemplateDao.selectPageByExample(query));
        }

        sysTemplatePage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
        return sysTemplatePage;
    }

}
