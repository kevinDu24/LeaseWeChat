package com.tm.leasewechat.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangbiao on 2016/12/6.
 */
@Repository
public class RedisRepository {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource(name="redisTemplate")
    private ValueOperations<Object, Object> valOps;

    public void save(Object key, Object value, int time){

        valOps.set(key, value, time, TimeUnit.SECONDS);
    }

    public void save(Object key, Object value){
        valOps.set(key, value);
    }

    public Object get(Object key){

        return valOps.get(key);
    }

    public void delete(Object key){
        redisTemplate.delete(key);
    }
}
