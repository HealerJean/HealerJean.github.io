package com.healerjean.proj.hotcache;


import com.healerjean.proj.hotcache.service.snapshot.FullSnapshotService;
import com.healerjean.proj.hotcache.datasource.impl.UserTagDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 测试运行器
 */
@Slf4j
@Component
public class TestRunner implements CommandLineRunner {

    @Autowired
    private FullSnapshotService fullSnapshotService;


    @Autowired
    private UserTagDataSource userTagDataSource;


    @Override
    public void run(String... args) throws Exception {
        log.info("开始生成用户标签快照...");
        fullSnapshotService.generate("user_tag");
        log.info("所有快照生成完成！");
    }

}


