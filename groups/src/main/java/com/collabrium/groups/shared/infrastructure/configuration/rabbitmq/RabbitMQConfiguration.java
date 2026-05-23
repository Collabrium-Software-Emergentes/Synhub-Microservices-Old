package com.collabrium.groups.shared.infrastructure.configuration.rabbitmq;

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
  public static final String GROUPS_EXCHANGE = "groups.exchange";

  // =========================
  // ROUTING KEYS
  // =========================
  public static final String USER_LEADER_CREATED_KEY = "user.leader.created";
  public static final String LEADER_CREATED_KEY = "leader.created";
  public static final String INVITATION_ACCEPTED_KEY = "invitation.accepted";

  // =========================
  // QUEUES
  // =========================
  public static final String USER_LEADER_CREATED_QUEUE = "groups.user.leader.created.queue";

  // =========================
  // EXCHANGE BEANS
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
  public Queue userLeaderCreatedQueue() {
    return new Queue(USER_LEADER_CREATED_QUEUE);
  }

  // =========================
  // BINDING
  // =========================
  @Bean
  public Binding userLeaderCreatedBinding(
      Queue userLeaderCreatedQueue,
      TopicExchange iamExchange
  ) {
    return BindingBuilder
        .bind(userLeaderCreatedQueue)
        .to(iamExchange)
        .with(USER_LEADER_CREATED_KEY);
  }

  @Bean
  public Jackson2JsonMessageConverter jsonConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
