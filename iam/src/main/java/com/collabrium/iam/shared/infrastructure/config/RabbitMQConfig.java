package com.collabrium.iam.shared.infrastructure.config;

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
  public static final String IAM_EXCHANGE = "iam.exchange";
  public static final String GROUPS_EXCHANGE = "groups.exchange";

  // =========================
  // ROUTING KEYS
  // =========================
  public static final String USER_LEADER_CREATED_KEY = "user.leader.created";
  public static final String USER_MEMBER_CREATED_KEY = "user.member.created";

  public static final String LEADER_CREATED_KEY = "leader.created";

  // =========================
  // QUEUES
  // =========================
  public static final String LEADER_CREATED_QUEUE = "iam.leader.created.queue";

  // =========================
  // EXCHANGE BEAN
  // =========================
  @Bean
  public TopicExchange iamExchange() {
    return new TopicExchange(IAM_EXCHANGE);
  }

  @Bean TopicExchange groupsExchange() {
    return new TopicExchange(GROUPS_EXCHANGE);
  }

  // =========================
  // QUEUE
  // =========================
  @Bean
  public Queue leaderCreatedQueue() {
    return new Queue(LEADER_CREATED_QUEUE);
  }

  // =========================
  // BINDING
  // =========================
  @Bean
  public Binding leaderCreatedBinding(
      Queue leaderCreatedQueue,
      TopicExchange groupsExchange
  ) {
    return BindingBuilder
        .bind(leaderCreatedQueue)
        .to(groupsExchange)
        .with(LEADER_CREATED_KEY);
  }

  @Bean
  public Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
