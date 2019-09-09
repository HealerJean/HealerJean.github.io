/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.hlj.proj.data.dao.mybatis.manager.user;

import java.util.List;

import com.hlj.proj.data.common.paging.Pagenation;
import com.hlj.proj.data.pojo.user.Tuser;
import com.hlj.proj.data.pojo.user.TuserPage;
import com.hlj.proj.data.pojo.user.TuserQuery;
import com.hlj.proj.data.dao.mybatis.dao.user.TuserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author zhangyujin
 * @ClassName: TuserManager
 * @date 2099/1/1
 * @Description: TuserManager
 */
@Component("tuserManager")
public class TuserManager {

    @Autowired
    @Qualifier("tuserDao")
    private TuserDao tuserDao;

    public TuserDao getDao() {
        return this.tuserDao;
    }

    public int save(Tuser tUser) {
        int cnt = 0;
        if (tUser.getId() == null) {
            cnt = tuserDao.insertSelective(tUser);
        } else {
            cnt = tuserDao.updateByPrimaryKeySelective(tUser);
        }
        return cnt;
    }

    public int update(Tuser tUser) {
        return tuserDao.updateByPrimaryKey(tUser);
    }

    public int updateSelective(Tuser tUser) {
        return tuserDao.updateByPrimaryKeySelective(tUser);
    }

    public int insert(Tuser tUser) {
        return tuserDao.insert(tUser);
    }

    public int insertSelective(Tuser tuser) {
        return tuserDao.insertSelective(tuser);
    }

    public int batchInsert(List<Tuser> list) {
        return tuserDao.batchInsert(list);
    }

    public Tuser findById(long id) {
        return tuserDao.selectByPrimaryKey(id);
    }

    public Tuser findByQueryContion(TuserQuery query) {
        List<Tuser> list = tuserDao.selectByExample(query);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public List<Tuser> queryList(TuserQuery query) {
        return tuserDao.selectByExample(query);
    }

    public int deleteById(long id) {
        return tuserDao.deleteByPrimaryKey(id);
    }

    public int delete(TuserQuery query) {
        return tuserDao.deleteByExample(query);
    }

    public TuserPage queryPageList(TuserQuery query) {
        TuserPage tuserPage = new TuserPage();
        Integer itemCount = tuserDao.countByExample(query);
        query.setItemCount(itemCount);

        if (itemCount == 0) {
            tuserPage.setValues(null);
        } else {
            tuserPage.setValues(tuserDao.selectPageByExample(query));
        }

        tuserPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
        return tuserPage;
    }

}
