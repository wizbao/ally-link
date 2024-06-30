package com.abao.allylink.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonClientConfig {

    private String host;

    private int port;

    private int database;

    private String password;

    @Bean
    public RedissonClient redissonClient(){
        // 配置
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s",host,port);
        config.useSingleServer().setAddress(redisAddress).setDatabase(database).setPassword(password);
        // 创建RedisClient对象
        return Redisson.create(config);
    }
}