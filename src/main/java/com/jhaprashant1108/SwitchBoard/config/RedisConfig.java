package com.jhaprashant1108.SwitchBoard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Autowired
    EnvConfig envConfig;

    @Bean
    public RedisStandaloneConfiguration config() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(envConfig.getRedisHostName());
        config.setPort(envConfig.getRedisPort());
        config.setUsername(envConfig.getRedisUserName());
        config.setPassword(envConfig.getRedisPassword());
        return config;
    }


    @Bean
    public LettuceConnectionFactory connectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config());
        factory.afterPropertiesSet();
        return factory;
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisConnectionFactory factory = connectionFactory();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        try {
            factory.getConnection().ping();
        } catch (Exception e) {
            throw new IllegalStateException(ErrorMessages.Connection.REDIS_CONNECTION_FAILED, e);
        }
        return template;
    }


}
