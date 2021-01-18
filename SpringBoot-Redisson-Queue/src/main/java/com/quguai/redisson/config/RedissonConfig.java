package com.quguai.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;

@Configuration
public class RedissonConfig {

    @Autowired
    private Environment environment;

    @Bean
    public RedissonClient config() throws IOException {
        // 方式一
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.1.115:6379").setKeepAlive(true);

        // 方式二
        //Config config = Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream());
        return Redisson.create(config);
    }
}
