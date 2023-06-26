package com.healerjean.proj.service.impl;

import com.healerjean.proj.manager.impl.DemoEntityManger;
import com.healerjean.proj.pojo.DemoEntity;
import com.healerjean.proj.service.DemoEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2021/12/17  4:32 下午.
 * @description
 */
@Slf4j
@Service
public class DemoEntityServiceImpl implements DemoEntityService {

    @Resource
    private DemoEntityManger demoEntityManger;

    private static int BATCH_DEAL_NUM = 2000;

    /**
     * 数据插入
     */
    @Override
    public void demoEntityBigDataInsert() {
        int count = 0 ;
        List<DemoEntity> list = new ArrayList<>();
        for (int i = 1; i < 2000000; i++){
            DemoEntity demoEntity = new DemoEntity();
            demoEntity.setName("name"+i);
            demoEntity.setPhone("188"+i);
            demoEntity.setEmail("h@gmail.com"+i);
            demoEntity.setAge(i);
            demoEntity.setStatus("10");
            demoEntity.setCreateUser(Long.valueOf(i));
            demoEntity.setCreateName("createName");
            demoEntity.setCreateTime(new Date());
            demoEntity.setUpdateUser(Long.valueOf(i));
            demoEntity.setUpdateName("updateName");
            demoEntity.setUpdateTime(new Date());
            list.add(demoEntity);
            count++;
            if (count == BATCH_DEAL_NUM){
                demoEntityManger.saveBatch(list);
                list.clear();
                count = 0;
            }
        }
    }

}
