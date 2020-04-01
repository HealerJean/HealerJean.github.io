package com.healerjean.proj.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healerjean.proj.pojo.DemoEntity;
import com.healerjean.proj.pojo.DemoEntityQuery;

import java.util.List;

/**
 * 作者 ：HealerJean
 * 日期 ：2018/11/14  下午12:54.
 * 类描述：
 */
public interface DemoEntityMapper extends BaseMapper<DemoEntity> {


    Long countQueryContion(DemoEntityQuery query);

    DemoEntity findByQueryContion(DemoEntityQuery query);

    List<DemoEntity> queryList(DemoEntityQuery query);


}
