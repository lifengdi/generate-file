package com.lifengdi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author 李锋镝
 * @date Create at 11:33 2018/9/10
 */
@Component
public class DistributedLocks {
    /**
     * 获得锁有效时间
     */
    @Value("${lock.timeout:3000}")
    private long lockTimeout;
    @Value("${lock.sleep.interval.time:30}")
    private long sleepIntervalTime;

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public DistributedLocks(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 尝试获取锁，立即返回结果
     *
     * @param key 锁key
     * @return boolean
     */
    public boolean tryLock(String key) {
        if (StringUtils.isEmpty(key)) {
            return true;
        }
        return lock(key, lockTimeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 尝试获取锁，立即返回结果(锁有效时间60min)
     *
     * @param key 锁key
     * @return boolean
     */
    public boolean sixtyMinutesLock(String key) {
        if (StringUtils.isEmpty(key)) {
            return true;
        }
        return lock(key, 60, TimeUnit.MINUTES);
    }

    /**
     * 尝试获取锁，立即返回结果，指定锁过期时间
     *
     * @param key key
     * @param time 锁过期时间
     * @param unit 时间单位
     * @return boolean
     */
    public boolean lock(String key, long time, TimeUnit unit) {
        long lockTime = unit.toMillis(time);
        final ValueOperations<String, String> ops = redisTemplate.opsForValue();
        if (ops.setIfAbsent(key, getCacheValue(lockTime))) {
            return true;
        }
        String currentValue = ops.get(key);

        // 如果currentValue为空，说明锁拥有者已经释放掉锁了，可以进行再次尝试
        if (StringUtils.isEmpty(currentValue)) {
            return ops.setIfAbsent(key, getCacheValue(lockTime));
        }

        //判断上一个锁是否到期，到期尝试获取锁
        if (System.currentTimeMillis() >= getExpireTime(currentValue)) {
            //多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，它才有权利获取锁
            String oldValue = ops.getAndSet(key, getCacheValue(lockTime));
            return currentValue.equals(oldValue);
        }
        return false;
    }

    /**
     * 尝试获取锁，如果成功，立即返回，如果一直失败，等到超时之后返回
     *
     * @param key     锁key
     * @param timeout 等待时间
     * @param unit    TimeUnit
     * @return boolean
     * @throws InterruptedException InterruptedException
     */
    public boolean tryLock(String key, long timeout, TimeUnit unit) throws InterruptedException {

        long nanosTimeout = unit.toNanos(timeout);

        long lastTime = System.nanoTime();
        do {
            if (tryLock(key)) {
                return true;
            }

            long now = System.nanoTime();
            nanosTimeout -= now - lastTime;
            lastTime = now;

            //响应中断
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        } while (nanosTimeout <= 0L);

        return false;
    }

    /**
     * 获取锁，如果成功，立即返回，如果失败，则不停的尝试，直到成功为止
     *
     * @param key 锁key
     * @throws InterruptedException InterruptedException
     */
    public void lock(String key) throws InterruptedException {

        while (true) {
            if (tryLock(key)) {
                return;
            }

            //响应中断
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        }
    }


    /**
     * 释放锁
     *
     * @param key key
     */
    public void unlock(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 获取当期线程缓存value
     *
     * @param timeout 超时时间
     * @return 缓存值
     */
    private String getCacheValue(long timeout) {
        return Thread.currentThread().getName() + ";" + String.valueOf(System.currentTimeMillis() + timeout);
    }

    /**
     * 获取过期时间
     *
     * @param cacheValue 缓存值
     * @return 过期时间
     */
    private long getExpireTime(String cacheValue) {
        String[] values = cacheValue.split(";");
        return values.length > 1 ? Long.parseLong(values[1]) : 0L;
    }

}
