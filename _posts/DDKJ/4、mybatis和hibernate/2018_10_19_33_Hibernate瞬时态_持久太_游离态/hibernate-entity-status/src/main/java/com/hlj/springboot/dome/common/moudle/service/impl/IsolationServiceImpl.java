package com.hlj.springboot.dome.common.moudle.service.impl;

import com.hlj.springboot.dome.common.entity.DemoEntity;
import com.hlj.springboot.dome.common.entity.repository.DemoEntityRepository;
import com.hlj.springboot.dome.common.moudle.service.IsolationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/1/23  下午7:35.
 * 类描述：
 */
@Service
@Slf4j
public class IsolationServiceImpl  implements IsolationService {

    @Resource
    private DemoEntityRepository demoEntityRepository ;



    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public DemoEntity transRequirs(Long id) {
        DemoEntity demoEntity =  demoEntityRepository.findOne(id);
        demoEntity.setName("transRequirs");
        return demoEntityRepository.save(demoEntity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public DemoEntity transRequirsNew(Long id) {
        DemoEntity demoEntity =  demoEntityRepository.findOne(id);
        demoEntity.setName("transRequirsNew");
        return demoEntityRepository.save(demoEntity);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public DemoEntity isoLationReadCommited(Long id) {
        return demoEntityRepository.findOne(id);
    }
}
