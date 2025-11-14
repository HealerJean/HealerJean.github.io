package com.healerjean.proj.hotcache;


import com.alibaba.fastjson.JSON;
import com.healerjean.proj.hotcache.datasource.impl.UserTagDataSource;
import com.healerjean.proj.hotcache.incr.IncrementalLuaWriteService;
import com.healerjean.proj.hotcache.incr.IncrementalWriteService;
import com.healerjean.proj.hotcache.model.UserTag;
import com.healerjean.proj.hotcache.service.pull.FullLoadToMemoryService;
import com.healerjean.proj.hotcache.service.pull.IncrementLoadToMemoryService;
import com.healerjean.proj.hotcache.service.snapshot.FullSnapshotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 测试运行器
 */
@Slf4j
@Component
public class TestRunner implements CommandLineRunner {

    @Autowired
    private FullSnapshotService fullSnapshotService;

    @Autowired
    private IncrementalWriteService incrementalWriteService;

    /**
     * incrementalLuaWriteService
     */
    @Resource
    private IncrementalLuaWriteService incrementalLuaWriteService;

    @Resource
    private FullLoadToMemoryService fullLoadToMemoryService;

    @Resource
    private IncrementLoadToMemoryService incrementLoadToMemoryService;

    /**
     * userTagDataSource
     */
    @Resource
    private UserTagDataSource userTagDataSource;

    @Override
    public void run(String... args) throws Exception {
        // fullSnapshot();
        //
        // writeIncremental();

        // writeLuaIncremental();


        // FullSnapshotToMemory();

        // pullIncrementalSinceBound();

    }

    private String fullSnapshot() throws Exception {
        log.info("开始生成用户标签快照...");
        String version = fullSnapshotService.generate("user_tag", LocalDateTime.now());
        log.info("所有快照生成完成！");
        return version;
    }


    public void writeIncremental() {
        log.info("开始生成用户标签增量快照...");
        List<UserTag> userTags = userTagDataSource.loadBatch(null);
        userTags.forEach(userTag -> incrementalWriteService.write(JSON.toJSONString(userTag), "user_tag"));
        log.info("所有增量快照生成完成！");
    }


    public void writeLuaIncremental() {
        log.info("开始生成用户标签LUA增量快照...");
        List<UserTag> userTags = userTagDataSource.loadBatch(null);
        userTags.forEach(userTag -> incrementalWriteService.write(JSON.toJSONString(userTag), "user_tag"));
        log.info("所有LUA增量快照生成完成！");
    }


    public void FullSnapshotToMemory() throws Exception {
        log.info("开始加载用户标签快照到内存...");
        fullLoadToMemoryService.loadLatestFullSnapshotToMemory("user_tag");
        log.info("加载用户标签快照到内存完成");
    }


    public void pullIncrementalSinceBound(){
        log.info("开始增量加载用户标签快照到内存...");
        incrementLoadToMemoryService.pullIncrementalByLowerBound("user_tag");
        log.info("增量加载用户标签快照到内存完成");

    }
}


