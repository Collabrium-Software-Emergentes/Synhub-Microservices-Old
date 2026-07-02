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
  public static final String INVITATION_ACCEPTED_KEY = "invitation.accepted";
  public static final String INVITATION_CREATED_KEY = "invitation.created";
  public static final String MEMBER_REMOVED_FROM_GROUP_KEY = "member.removed.from.group";
  public static final String GROUP_DELETED_KEY = "group.deleted";

  // =========================
  // QUEUES
  // =========================
  public static final String GROUP_CREATED_QUEUE = "groups.group.created.queue";
  public static final String INVITATION_ACCEPTED_QUEUE = "groups.invitation.accepted.queue";
  public static final String INVITATION_CREATED_QUEUE = "groups.invitation.created.queue";
  public static final String MEMBER_REMOVED_FROM_GROUP_QUEUE = "groups.member.removed.from.group.queue";
  public static final String GROUP_DELETED_QUEUE = "groups.group.deleted.queue";

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

  @Bean
  public Queue invitationAcceptedQueue() {
    return new Queue(INVITATION_ACCEPTED_QUEUE);
  }

  @Bean
  public Queue invitationCreatedQueue() {
    return new Queue(INVITATION_CREATED_QUEUE);
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
  public Binding invitationCreatedBinding(
      Queue invitationCreatedQueue,
      TopicExchange groupsExchange
  ) {
    return BindingBuilder
        .bind(invitationCreatedQueue)
        .to(groupsExchange)
        .with(INVITATION_CREATED_KEY);
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