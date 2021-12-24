package com.healerjean.proj.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.healerjean.proj.dao.mapper.DemoEntityMapper;
import com.healerjean.proj.manager.DemoEntityMangerService;
import com.healerjean.proj.pojo.DemoEntity;
import org.springframework.stereotype.Service;

/**
 * @author zhangyujin
 * @date 2021/12/17  4:50 下午.
 * @description
 */
@Service
public class DemoEntityManger   extends ServiceImpl<DemoEntityMapper, DemoEntity> implements DemoEntityMangerService  {

}
