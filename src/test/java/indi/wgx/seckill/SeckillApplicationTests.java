package indi.wgx.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SeckillApplicationTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisScript<Boolean> redisScript;


    /**
     * 用 redisTemplate 实现分布式锁
     */
    @Test
    void testLock01() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        // 占位，只有key不存在，才能设值成功
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        if (isLock == null) {
            return;
        }
        // 模拟操作
        if (isLock) {
            valueOperations.set("name", "xiaowei");
            String name = valueOperations.get("name");
            System.out.println("name: " + name);
            // 操作结束，删除锁
            redisTemplate.delete("k1");
        } else {
            System.out.println("有线程正在使用,请稍微再试");
        }
    }


    /**
     * 获取锁、比较锁、删除锁 这三个不是原子性操作
     */
    @Test
    void testLock02() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        // 给锁添加一个过期时间，防止应用在运行过程中抛出异常导致锁无法正常释放
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1", 5, TimeUnit.SECONDS);
        if (isLock == null) {
            return;
        }
        // 模拟操作
        if (isLock) {
            valueOperations.set("name", "xiaowei");
            String name = valueOperations.get("name");
            System.out.println("name: " + name);
            // 操作结束，删除锁
            redisTemplate.delete("k1");
        } else {
            System.out.println("有线程正在使用,请稍微再试");
        }
    }

    /**
     * 利用lock.lua脚本文件
     */
    @Test
    public void testLock3() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = UUID.randomUUID().toString();
        Boolean isLock = valueOperations.setIfAbsent("k1", value, 5, TimeUnit.SECONDS);
        if (isLock) {
            valueOperations.set("name", "xiaowei");
            String name =valueOperations.get("name");
            System.out.println("name=" + name);
            //操作结束，删除锁
            System.out.println(valueOperations.get("k1"));
            Boolean result = redisTemplate.execute(redisScript, Collections.singletonList("k1"), value);
            System.out.println(result);
        } else {
            System.out.println("有线程在使用，请稍后再试");
        }
    }
}
