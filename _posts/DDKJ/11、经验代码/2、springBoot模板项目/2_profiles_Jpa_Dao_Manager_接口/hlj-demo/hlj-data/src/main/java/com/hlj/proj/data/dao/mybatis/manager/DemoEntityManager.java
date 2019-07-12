/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.hlj.proj.data.dao.mybatis.manager;

import java.util.List;

import com.hlj.proj.data.common.paging.Pagenation;
import com.hlj.proj.data.dao.mybatis.dao.deme.DemoEntityDao;
import com.hlj.proj.data.pojo.demo.DemoEntity;
import com.hlj.proj.data.pojo.demo.DemoEntityPage;
import com.hlj.proj.data.pojo.demo.DemoEntityQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author zhangyujin
 * @ClassName: DemoEntityManager
 * @date 2099/1/1
 * @Description: DemoEntityManager
 */
@Component("demoEntityManager")
public class DemoEntityManager {

    @Autowired
    @Qualifier("demoEntityDao")
    private DemoEntityDao demoEntityDao;

    public DemoEntityDao getDao() {
        return this.demoEntityDao;
    }

    public int save(DemoEntity demoEntity) {
        int cnt = 0;
        if (demoEntity.getId() == null) {
            cnt = demoEntityDao.insertSelective(demoEntity);
        } else {
            cnt = demoEntityDao.updateByPrimaryKeySelective(demoEntity);
        }
        return cnt;
    }

    public int update(DemoEntity demoEntity) {
        return demoEntityDao.updateByPrimaryKey(demoEntity);
    }

    public int updateSelective(DemoEntity demoEntity) {
        return demoEntityDao.updateByPrimaryKeySelective(demoEntity);
    }

    public int insert(DemoEntity demoEntity) {
        return demoEntityDao.insert(demoEntity);
    }

    public int insertSelective(DemoEntity demoEntity) {
        return demoEntityDao.insertSelective(demoEntity);
    }

    public int batchInsert(List<DemoEntity> list) {
        return demoEntityDao.batchInsert(list);
    }

    public DemoEntity findById(long id) {
        return demoEntityDao.selectByPrimaryKey(id);
    }

    public DemoEntity findByQueryContion(DemoEntityQuery query) {
        List<DemoEntity> list = demoEntityDao.selectByExample(query);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public List<DemoEntity> queryList(DemoEntityQuery query) {
        return demoEntityDao.selectByExample(query);
    }

    public int deleteById(long id) {
        return demoEntityDao.deleteByPrimaryKey(id);
    }

    public int delete(DemoEntityQuery query) {
        return demoEntityDao.deleteByExample(query);
    }

    public DemoEntityPage queryPageList(DemoEntityQuery query) {
        DemoEntityPage demoEntityPage = new DemoEntityPage();
        Integer itemCount = demoEntityDao.countByExample(query);
        query.setItemCount(itemCount);

        if (itemCount == 0) {
            demoEntityPage.setValues(null);
        } else {
            demoEntityPage.setValues(demoEntityDao.selectPageByExample(query));
        }

        demoEntityPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
        return demoEntityPage;
    }

}
