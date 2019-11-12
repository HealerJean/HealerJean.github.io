package com.hlj.moudle.demo.service;

import com.hlj.entity.db.demo.DemoEntity;

import java.util.List;

/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/17  下午2:39.
 */
public interface DemoEntityService {

    DemoEntity addDemoEntity(DemoEntity demoEntity);

    List<DemoEntity> findAll();

    DemoEntity findById(Long id);

}
