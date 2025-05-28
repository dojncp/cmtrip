package com.sc2.cmtrip;

import com.sc2.cmtrip.entity.CtUser;
import com.sc2.cmtrip.mapper.CtUserMapper;
import com.sc2.cmtrip.service.CtUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class CmtripApplicationTests {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void redisTest() {

        // Execute Redis operation by RedisTemplate 使用RedisTemplate执行Redis操作
        redisTemplate.opsForValue().set("key", "value");

        // Read 读取
        String retrieveValue = redisTemplate.opsForValue().get("key");
        System.out.println(retrieveValue);

    }

}
