package com.collabrium.media.media.shared.infrastructure.configuration.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

  // =========================
  // EXCHANGE
  // =========================
  public static final String GROUPS_EXCHANGE = "groups.exchange";

  // =========================
  // ROUTING KEYS
  // =========================
  public static final String GROUP_DELETED_KEY = "group.deleted";

  // =========================
  // QUEUES
  // =========================
  public static final String GROUP_DELETED_QUEUE = "media.group.deleted.queue";

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
  public Queue groupDeletedQueue() {
    return new Queue(GROUP_DELETED_QUEUE);
  }

  // =========================
  // BINDING
  // =========================
  @Bean
  public Binding groupDeletedBinding(
      Queue groupDeletedQueue,
      TopicExchange groupsExchange
  ) {
    return BindingBuilder
        .bind(groupDeletedQueue)
        .to(groupsExchange)
        .with(GROUP_DELETED_KEY);
  }

  @Bean
  public Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}