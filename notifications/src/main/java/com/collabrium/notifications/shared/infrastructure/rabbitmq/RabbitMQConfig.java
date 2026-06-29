package com.collabrium.notifications.shared.infrastructure.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  // =========================
  // EXCHANGE
  // =========================
  public static final String GROUPS_EXCHANGE = "groups.exchange";

  // =========================
  // ROUTING KEYS
  // =========================
  public static final String GROUP_CREATED_KEY = "group.created";

  // =========================
  // QUEUES
  // =========================
  public static final String GROUP_CREATED_QUEUE = "groups.group.created.queue";

  // =========================
  // EXCHANGE BEANS
  // =========================
  @Bean
  public TopicExchange groupsExchange() {
    return new TopicExchange(GROUPS_EXCHANGE);
  }

  // =========================
  // QUEUE
  // =========================
  @Bean
  public Queue groupCreatedQueue() {
    return new Queue(GROUP_CREATED_QUEUE);
  }

  // =========================
  // BINDING
  // =========================
  @Bean
  public Binding groupCreatedBinding(
      Queue groupCreatedQueue,
      TopicExchange groupsExchange
  ) {
    return BindingBuilder
        .bind(groupCreatedQueue)
        .to(groupsExchange)
        .with(GROUP_CREATED_KEY);
  }

  @Bean
  public Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}