package com.healerjean.proj.data.bo;

import com.healerjean.proj.common.contants.RedisConstants;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.compress.utils.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * BigKeyBO
 *
 * @author zhangyujin
 * @date 2024/2/7
 */
@Accessors(chain = true)
@Data
public class BigKeyDataBO<T> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7560782834954336712L;

    /**
     * 缓存key
     */
    private RedisConstants.BigCacheEnum cacheEnum;

    /**
     * key
     */
    private String keyName;
    /**
     * value
     */
    private String keyValue;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 小key缓存索引，默认从0开始
     */
    private Long index = 0L;

    /**
     * 查询是否结束
     */
    private Boolean endFlag = false;

    /**
     * 待缓存对象
     */
    private List<T> readyCacheList = Lists.newArrayList();

    /**
     * 总数据
     */
    private List<T> data = Lists.newArrayList();

    /**
     * 添加-待缓存记录
     *
     * @param readyList readyList
     */
    public void addReadyCacheRecords(List<T> readyList) {
        this.getReadyCacheList().addAll(readyList);
        data.addAll(readyList);
    }


}
