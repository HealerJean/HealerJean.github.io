package com.healerjean.proj.hotcache.datasource.impl;

import com.healerjean.proj.hotcache.enums.DatesetKeyEnum;
import com.healerjean.proj.hotcache.model.UserTag;
import com.healerjean.proj.hotcache.datasource.SnapshotDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserTagDataSource
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
@Component("user_tag")
@Slf4j
public class UserTagDataSource implements SnapshotDataSource<UserTag> {

    @Override
    public DatesetKeyEnum getDatesetKeyEnum() {
        return DatesetKeyEnum.USER_TAG;
    }

    @Override
    public List<UserTag> loadBatch(Object lastKey) {
        // 模拟从数据库加载数据
        List<UserTag> batch = new ArrayList<>();
        String startId = lastKey != null ? (String) lastKey : "0";
        long start = Long.parseLong(startId);
        long estimateTotalCount = estimateTotalCount();
        // 模拟加载100万条数据
        if (start >= estimateTotalCount) {
            return new ArrayList<>();
        }

        int batchSize = 10000;
        for (int i = 0; i < batchSize && (start + i + 1) <= estimateTotalCount; i++) {
            String userId = String.format("%010d", start + i + 1);
            Map<String, String> tags = new HashMap<>();
            tags.put("age", String.valueOf(20 + ((start + i) % 50)));
            tags.put("gender", ((start + i) % 2 == 0) ? "M" : "F");
            tags.put("city", "City" + (((start + i) % 100) + 1));
            tags.put("level", "Level" + (((start + i) % 5) + 1));
            UserTag userTag = new UserTag();
            userTag.setUserId(userId);
            userTag.setTags(tags);
            batch.add(userTag);
        }

        log.debug("加载批次: start={}, size={}", startId, batch.size());
        return batch;
    }

    @Override
    public Object extractKey(UserTag record) {
        return record.getUserId();
    }

    /**
     * 模拟总数
     */
    @Override
    public long estimateTotalCount() {
        return 5000000L;
    }
}
