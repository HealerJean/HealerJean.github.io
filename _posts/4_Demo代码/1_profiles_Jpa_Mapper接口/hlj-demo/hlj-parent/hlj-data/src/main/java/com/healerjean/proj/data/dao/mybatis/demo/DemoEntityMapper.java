package com.healerjean.proj.data.dao.mybatis.demo;

import com.healerjean.proj.data.dao.mybatis.demo.query.DemoEntityQuery;
import com.healerjean.proj.data.pojo.demo.DemoEntity;

import java.util.List;

/**
 * 作者 ：HealerJean
 * 日期 ：2018/11/14  下午12:54.
 * 类描述：
 */
public interface DemoEntityMapper {


    Long countQueryContion(DemoEntityQuery query) ;

    DemoEntity findByQueryContion(DemoEntityQuery query);

    List<DemoEntity> queryList(DemoEntityQuery query) ;


}
