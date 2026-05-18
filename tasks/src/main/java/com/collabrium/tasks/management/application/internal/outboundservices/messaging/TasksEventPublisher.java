package com.collabrium.tasks.management.application.internal.outboundservices.messaging;

import com.collabrium.tasks.management.domain.model.events.MemberCreatedEvent;

public interface TasksEventPublisher {

  void publishMemberCreated(MemberCreatedEvent event);
}