package com.ucar.smadmin.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisComponent {
    //操作字符串的template，StringRedisTemplate是RedisTemplate的一个子集
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private Logger logger = LoggerFactory.getLogger(RedisComponent.class);
    //RedisTemplate可以进行所有的操作
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    public void set(String key, String value, Long timeout, TimeUnit unit) {
        ValueOperations<String, String> ops = this.stringRedisTemplate.opsForValue();
        boolean bExistent = this.stringRedisTemplate.hasKey(key);
        if (bExistent) {
            logger.info("this key is bExistent!");
        } else {
            ops.set(key, value, timeout, unit);
        }
    }

    public String get(String key) {
        return this.stringRedisTemplate.opsForValue().get(key);
    }

    public void del(String key) {
        this.stringRedisTemplate.delete(key);
    }

    public void sentinelSet(String key, Object object) {
        redisTemplate.opsForValue().set(key, object);
    }

    public String sentinelGet(String key) {
        return String.valueOf(redisTemplate.opsForValue().get(key));
    }

}
