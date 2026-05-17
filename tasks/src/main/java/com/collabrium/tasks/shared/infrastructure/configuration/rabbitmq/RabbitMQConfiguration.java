package com.collabrium.tasks.shared.infrastructure.configuration.rabbitmq;

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
  public static final String IAM_EXCHANGE = "iam.exchange";
  public static final String TASKS_EXCHANGE = "tasks.exchange";

  // =========================
  // ROUTING KEYS
  // =========================
  public static final String USER_MEMBER_CREATED_KEY = "user.member.created";
  public static final String MEMBER_CREATED_KEY = "member.created";

  // =========================
  // QUEUES
  // =========================
  public static final String USER_MEMBER_CREATED_QUEUE = "tasks.user.member.created.queue";

  // =========================
  // EXCHANGE BEAN
  // =========================
  @Bean
  public TopicExchange iamExchange() {
    return new TopicExchange(IAM_EXCHANGE);
  }

  @Bean TopicExchange tasksExchange() {
    return new TopicExchange(TASKS_EXCHANGE);
  }

  // =========================
  // QUEUE
  // =========================
  @Bean
  public Queue userMemberCreatedQueue() {
    return new Queue(USER_MEMBER_CREATED_QUEUE);
  }

  // =========================
  // BINDING
  // =========================
  @Bean
  public Binding userMemberCreatedBinding(
      Queue userMemberCreatedQueue,
      TopicExchange tasksExchange
  ) {
    return BindingBuilder
        .bind(userMemberCreatedQueue)
        .to(tasksExchange)
        .with(USER_MEMBER_CREATED_KEY);
  }

  @Bean
  public Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}