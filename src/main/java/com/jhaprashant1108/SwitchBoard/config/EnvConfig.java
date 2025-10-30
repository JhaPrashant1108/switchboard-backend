package com.jhaprashant1108.SwitchBoard.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class EnvConfig {


    @Value("${redis.hostName}")
    private String redisHostName;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.userName}")
    private String redisUserName;

    @Value("${redis.password}")
    private String redisPassword;



    @Value("${rabbitmq.hostName}")
    private String rabbitmqHostName;

    @Value("${rabbitmq.port}")
    private int rabbitmqPort;

    @Value("${rabbitmq.userName}")
    private String rabbitmqUserName;

    @Value("${rabbitmq.virtualHost}")
    private String rabbitmqVirtualHost;

    @Value("${rabbitmq.password}")
    private String rabbitmqPassword;

    @Value("${rabbitmq.sslProtocolEnabled}")
    private boolean rabbitmqSslProtocolEnabled;

    @Value("${rabbitmq.automaticRecoveryEnabled}")
    private boolean rabbitmqAutomaticRecoveryEnabled;

    @Value("${rabbitmq.requestedHeartBeat}")
    private int rabbitmqRequestedHeartBeat;

    @Value("${rabbitmq.connectionTimeout}")
    private int rabbitmqConnectionTimeout;






}
