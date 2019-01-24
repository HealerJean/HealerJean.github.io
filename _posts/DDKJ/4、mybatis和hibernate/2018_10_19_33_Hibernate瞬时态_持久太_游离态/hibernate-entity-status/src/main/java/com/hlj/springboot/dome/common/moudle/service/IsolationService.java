package com.hlj.springboot.dome.common.moudle.service;

import com.hlj.springboot.dome.common.entity.DemoEntity;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/1/23  下午7:34.
 * 类描述：
 */
public interface IsolationService {



    /**
     * 方法中的事物为入口方法的事物
     * @param id
     * @return
     */
    DemoEntity  transRequirs(Long id);

    /**
     * 开启一个事物
     * @param id
     * @return
     */
    DemoEntity  transRequirsNew (Long id);


    /**
     * 事物隔离级别为 读已提交（）
     * @param id
     * @return
     */
    DemoEntity   isoLationReadCommited(Long id) ;

}
