package com.hlj.springboot.dome.common.moudle.service;

import com.hlj.springboot.dome.common.entity.DemoEntity;
import com.hlj.springboot.dome.common.entity.repository.DemoEntityRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/1/23  下午8:02.
 * 类描述：
 */
@Service
public class IsolationStartService {

    @Resource
    private DemoEntityRepository demoEntityRepository ;

    @Resource
    private IsolationService isolationService ;

    public DemoEntity startTransactional(Long id) {
        DemoEntity demoEntity =  demoEntityRepository.findOne(id);
        demoEntity.setName("startTransactional"); //还没有保存到数据库中，因为事物还没有提交
        System.out.println(demoEntity);
        //开始独立事物直接保存数据，直接在数据库中有显示
        DemoEntity transRequirsNew =   isolationService.transRequirsNew(id);
        System.out.println(transRequirsNew);//DemoEntity(id=1, name=transRequirsNew, balance=null)

        //当前事物下保存数据，上面的额startTransactional 事物还没有提交，所以和一开始的状态是一致的
        demoEntity = isolationService.transRequirs(id);
        System.out.println(demoEntity) ;//DemoEntity(id=1, name=transRequirs, balance=null)

        //读取已经提交的数据
        demoEntity = isolationService.isoLationReadCommited(id);
        System.out.println(demoEntity);//DemoEntity(id=1, name=transRequirs, balance=null)
        return demoEntity; //最终数据库中就变成了  DemoEntity(id=1, name=transRequirs, balance=null)
    }
}
