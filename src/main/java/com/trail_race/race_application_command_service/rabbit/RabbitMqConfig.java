package com.trail_race.race_application_command_service.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
@Slf4j
public class RabbitMqConfig {

    @Bean
    public ConnectionFactory connectionFactory(RabbitProperties properties) {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
                properties.getHost(),
                properties.getPort());

        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());

        log.info("Creating connection factory with: " + properties.getUsername()
                + "@" + properties.getHost() + ":" + properties.getPort());
        return connectionFactory;
    }

    /**
     * Required for executing administration functions against an AMQP Broker
     */
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * The following is a complete declaration of an exchange, a queue and an exchange-queue binding
     */
    @Bean
    public TopicExchange raceExchange() {
        return new TopicExchange("race", true, false);
    }

    /**
     * This queue will be declared. This means it will be created if it does not exist. Once declared,
     * we can do something like the following:
     *
     * @RabbitListener(queues = "#{@myDurableQueue}")
     * @Transactional public void handleMyDurableQueueMessage(CustomDurableDto myMessage) {
     * // Anything you want!
     * // This can also return a non-void which will queue it back in to the queue attached to @RabbitListener
     * }
     */
    @Bean
    public Queue echoQueue() {
        return new Queue("echo", true, false, false);
    }

    @Bean
    public Queue commandQueue() {
        return new Queue("command", true, false, false);
    }

    /**
     * Binding must be declared between exchange and queue. This is sending each message to corresponding inbound
     * or outbound queue. If wish to fanout messages to multiple queues see example
     * <a href="https://www.rabbitmq.com/tutorials/tutorial-five-python">here</a>
     */
    @Bean
    public Binding echoExchangeBinding(TopicExchange raceExchange, Queue echoQueue) {
        return BindingBuilder.bind(echoQueue).to(raceExchange).with("to.*");
    }

    @Bean
    public Binding commandExchangeBinding(TopicExchange raceExchange, Queue commandQueue) {
        return BindingBuilder.bind(commandQueue).to(raceExchange).with("to.*");
    }
}
