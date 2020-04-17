package demo.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLock {
    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    public Boolean lock(String key,String value) {
        //key不存在返回true,该线程可以进行数据操作
        if(stringRedisTemplate.opsForValue().setIfAbsent(key,value)) {
            return true;
        }
        //超时状态下，多个客户端同时请求过来,竞争锁,双重校验
        String currValue = stringRedisTemplate.opsForValue().get(key);
        if(StringUtils.isNotEmpty(currValue) && Long.valueOf(currValue) < System.currentTimeMillis()) {
            String oldValue = stringRedisTemplate.opsForValue().getAndSet(key,value);
            if(StringUtils.isNotEmpty(oldValue) && oldValue.equals(currValue)) {
                return true;
            }
        }
        return false;
    }

    public void unlock(String key,String value) {
        String currValue = stringRedisTemplate.opsForValue().get(key);
        if(StringUtils.isNotEmpty(currValue) && StringUtils.equals(currValue,value)){
            stringRedisTemplate.opsForValue().getOperations().delete(key);
        }
    }
}
