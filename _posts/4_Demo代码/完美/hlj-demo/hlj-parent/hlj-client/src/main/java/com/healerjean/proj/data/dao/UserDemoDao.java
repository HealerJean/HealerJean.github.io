package com.healerjean.proj.data.dao;


import com.baomidou.mybatisplus.extension.service.IService;
import com.healerjean.proj.data.po.UserDemo;

import java.util.List;

/**
 * UserDemoDao
 *
 * @author zhangyujin
 * @date 2023/6/14  10:55
 */
public interface UserDemoDao extends IService<UserDemo> {


    /**
     * 根据唯一索引插入或更新
     *
     * @param userDemos userDemos
     * @return {@link boolean}
     */
    boolean saveOrUpdateBatchByUniqueKey(List<UserDemo> userDemos);

}
