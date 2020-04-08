package com;

import com.crud.UserDaoImpl;
import com.crud.UserVO;
import redis.clients.jedis.Jedis;

public class testMain {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        jedis.auth("wsuser");

        UserVO userDemo1 = new UserVO();
        userDemo1.setUserId("1001");
        userDemo1.setUserName("xiao ming");
        userDemo1.setPassword("123456");

        UserVO userDemo2 = new UserVO();
        userDemo2.setUserId("13579");
        userDemo2.setUserName("xiao hong");
        userDemo2.setPassword("123456");

        //连接redis服务
        UserDaoImpl redisService = new UserDaoImpl();

        redisService.insert(userDemo1);
        redisService.queryById(userDemo1.getUserId());

        redisService.insert(userDemo2);
        redisService.queryAll();

        userDemo1.setUserName("xiao fang");
        userDemo1.setPassword("123456");
        redisService.update(userDemo1);
        redisService.queryById(userDemo1.getUserId());

        redisService.delete(userDemo1);
        redisService.queryAll();

        redisService.queryByName("xiao");
    }
}
