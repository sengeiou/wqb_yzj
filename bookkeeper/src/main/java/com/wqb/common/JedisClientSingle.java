package com.wqb.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisClientSingle implements JedisClient {
    @Autowired
    JedisPool jedisPool;

    //@Autowired 是根据类型注入 sprign里面的
    //@Autowired是根据类型进行自动装配的。如果当Spring上下文中存在不止一个UserDao类型的bean时，
    //就会抛出BeanCreationException异常;如果Spring上下文中不存在UserDao类型的bean，
    //也会抛出BeanCreationException异常

    //注意 jedisPool是连接池
    //从连接池取到jedis后要关闭 不然过一会就会用完资源


    @Override
    ////获取指定 key 的值。如果 key 不存在，返回 nil
    public String get(String key) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String value = resource.get(key);
            return value;
        } finally {
            if (resource != null) {
                resource.close();
            }
        }

    }

    @Override
    public String set(String key, String value) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String set = resource.set(key, value);
            return set;
        } finally {
            if (resource != null) {
                resource.close();
            }
        }
    }

    @Override
    ////获取哈希表中指定字段的值
    public String hget(String hkey, String key) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            String hget = resource.hget(hkey, key);
            return hget;
        } finally {
            if (resource != null) {
                //注意：一定要关闭连接资源 否则资源会很快耗尽
                resource.close();
            }
        }
    }

    @Override
    ////为哈希表中的字段赋值
    public Long hset(String hkey, String key, String value) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            Long hsetnx = resource.hset(hkey, key, value);
            return hsetnx;
        } finally {
            if (resource != null) {
                resource.close();
            }
        }
    }

    @Override
    //注意 value 必须是整形
    public Long incr(String key) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            Long incr = resource.incr(key);
            return incr;
        } finally {
            if (resource != null) {
                resource.close();
            }
        }
    }

    @Override
    //设置key的到期时间
    public Long expire(String key, int seconds) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            Long expire = resource.expire(key, seconds);
            return expire;
        } finally {
            if (resource != null) {
                resource.close();
            }
        }
    }

    @Override
    public Long ttl(String key) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            Long ttl = resource.ttl(key);
            return ttl;
        } finally {
            if (resource != null) {
                resource.close();
            }
        }
        // TODO Auto-generated method stub

    }

    @Override
    //根据key删除
    public Long del(String key) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            Long del = resource.del(key);
            return del;
        } finally {
            if (resource != null) {
                resource.close();
            }
        }
    }


}
