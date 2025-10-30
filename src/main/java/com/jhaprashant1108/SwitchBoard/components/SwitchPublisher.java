package com.jhaprashant1108.SwitchBoard.components;


import com.jhaprashant1108.SwitchBoard.config.RabbitMQConfig;
import com.jhaprashant1108.SwitchBoard.models.MessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SwitchPublisher {


    @Autowired
    RabbitTemplate rabbitTemplate;

    public void send(MessageModel message) {
        String routingKeyValue = RabbitMQConfig.ROUTING_KEY + message.getSwitchDetails().getEnvironmentName() +"."+ message.getSwitchDetails().getApplicationName();
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                routingKeyValue,
                message
        );
        log.info("Sent message: {} to routingKeyValue : {} " , message.toString() , routingKeyValue);
    }


}
