package com.jhaprashant1108.SwitchBoard.config;

import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig {

    // Constants moved to AppConstants class for better configuration management
    public static final String EXCHANGE = AppConstants.RabbitMQ.EXCHANGE;
    public static final String ROUTING_KEY = AppConstants.RabbitMQ.ROUTING_KEY;
    public static final String QUEUE = AppConstants.RabbitMQ.QUEUE;

    @Autowired
    EnvConfig envConfig;


    @Bean
    public ConnectionFactory rabbitFactory() throws Exception{
        ConnectionFactory rabbitFactory = new ConnectionFactory();
        rabbitFactory.setHost(envConfig.getRabbitmqHostName());
        rabbitFactory.setPort(envConfig.getRabbitmqPort());
        rabbitFactory.setUsername(envConfig.getRabbitmqUserName());
        rabbitFactory.setVirtualHost(envConfig.getRabbitmqVirtualHost());
        rabbitFactory.setPassword(envConfig.getRabbitmqPassword());
        if(envConfig.isRabbitmqSslProtocolEnabled()) {
            rabbitFactory.useSslProtocol();
        }
        rabbitFactory.setAutomaticRecoveryEnabled(envConfig.isRabbitmqAutomaticRecoveryEnabled());
        rabbitFactory.setRequestedHeartbeat(envConfig.getRabbitmqRequestedHeartBeat());
        rabbitFactory.setConnectionTimeout(envConfig.getRabbitmqConnectionTimeout());
        return rabbitFactory;
    }


    @Bean
    public CachingConnectionFactory rabbitMqconnectionFactory(ConnectionFactory rabbitFactory) throws Exception {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitFactory);
        cachingConnectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        cachingConnectionFactory.setPublisherReturns(true);
        cachingConnectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        cachingConnectionFactory.setRequestedHeartBeat(envConfig.getRabbitmqRequestedHeartBeat());
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory rabbitMqconnectionFactory , MessageConverter jsonMessageConverter) throws Exception {
        RabbitTemplate template = new RabbitTemplate(rabbitMqconnectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        try {
            rabbitMqconnectionFactory.createConnection().createChannel(false);
            log.error("RabbitMQ connection established at startup");
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.Connection.RABBITMQ_CONNECTION_FAILED, e);
        }
        return template;
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

}
