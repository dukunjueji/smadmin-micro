package com.ucar.smadmin.common.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson配置
 *
 * @author DK
 */
@Configuration
public class RedissonConfig {

    private String host = "10.112.11.26";

    private String port = "6379";


    @Bean
    public RedissonClient getRedisson() {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        //添加主从配置
        // config.useMasterSlaveServers().setMasterAddress("").setPassword("").addSlaveAddress(new String[]{"",""});
        return Redisson.create(config);
    }

}