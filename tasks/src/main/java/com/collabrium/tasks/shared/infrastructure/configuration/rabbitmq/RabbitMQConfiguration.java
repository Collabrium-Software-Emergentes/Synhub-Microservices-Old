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
  public static final String GROUPS_EXCHANGE = "groups.exchange";

  // =========================
  // ROUTING KEYS
  // =========================
  public static final String USER_MEMBER_CREATED_KEY = "user.member.created";
  public static final String MEMBER_CREATED_KEY = "member.created";
  public static final String INVITATION_ACCEPTED_KEY = "invitation.accepted";
  public static final String MEMBER_LEFT_GROUP_KEY = "member.left.group";
  public static final String MEMBER_REMOVED_FROM_GROUP_KEY = "member.removed.from.group";
  public static final String GROUP_DELETED_KEY = "group.deleted";

  // =========================
  // QUEUES
  // =========================
  public static final String USER_MEMBER_CREATED_QUEUE = "tasks.user.member.created.queue";
  public static final String INVITATION_ACCEPTED_QUEUE = "tasks.invitation.accepted.queue";
  public static final String MEMBER_REMOVED_FROM_GROUP_QUEUE = "tasks.member.removed.from.group.queue";
  public static final String GROUP_DELETED_QUEUE = "tasks.group.deleted.queue";

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

  @Bean
  public TopicExchange groupsExchange() {
    return new TopicExchange(GROUPS_EXCHANGE);
  }

  // =========================
  // QUEUE
  // =========================
  @Bean
  public Queue userMemberCreatedQueue() {
    return new Queue(USER_MEMBER_CREATED_QUEUE);
  }

  @Bean
  public Queue invitationAcceptedQueue() {
    return new Queue(INVITATION_ACCEPTED_QUEUE);
  }

  @Bean
  public Queue memberRemovedFromGroupQueue() {
    return new Queue(MEMBER_REMOVED_FROM_GROUP_QUEUE);
  }

  @Bean
  public Queue groupDeletedQueue() {
    return new Queue(GROUP_DELETED_QUEUE);
  }

  // =========================
  // BINDING
  // =========================
  @Bean
  public Binding userMemberCreatedBinding(
      Queue userMemberCreatedQueue,
      TopicExchange iamExchange
  ) {
    return BindingBuilder
        .bind(userMemberCreatedQueue)
        .to(iamExchange)
        .with(USER_MEMBER_CREATED_KEY);
  }

  @Bean
  public Binding invitationAcceptedBinding(
      Queue invitationAcceptedQueue,
      TopicExchange groupsExchange
  ) {

    return BindingBuilder
        .bind(invitationAcceptedQueue)
        .to(groupsExchange)
        .with(INVITATION_ACCEPTED_KEY);
  }

  @Bean
  public Binding memberRemovedFromGroupBinding(
      Queue memberRemovedFromGroupQueue,
      TopicExchange groupsExchange
  ) {

    return BindingBuilder
        .bind(memberRemovedFromGroupQueue)
        .to(groupsExchange)
        .with(MEMBER_REMOVED_FROM_GROUP_KEY);
  }

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