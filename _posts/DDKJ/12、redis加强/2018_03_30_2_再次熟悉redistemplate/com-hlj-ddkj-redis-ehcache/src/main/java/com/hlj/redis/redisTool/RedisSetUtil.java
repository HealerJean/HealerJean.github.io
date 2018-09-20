package com.hlj.redis.redisTool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Set;

/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/19  下午1:54.
 */
@Service
@Slf4j
public class RedisSetUtil {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    /**
     * 添加集合
     * @param key
     */
    public void add(String key,Object... objects) {
        Arrays.stream(objects).forEach(object->{
            redisTemplate.opsForSet().add(key,object);
        });
    }

    /**
     * 根据key获取集合全部
     * @param key
     * @return
     */
    public Set<Object> get(String key) {
        return redisTemplate.opsForSet().members(key);
    }


    /**
     * 判断object是否存在
     */
    public boolean isExists(String key,Object object){
      return   redisTemplate.opsForSet().isMember(key,object );
    }


    /**
     * 删除set集合中的某个值
     * @param key
     * @param objects
     */
    public void deleteObject(String key,Object... objects){
        redisTemplate.opsForSet().remove(key,objects);
    }

    /**
     * 根据key 删除
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }




}
