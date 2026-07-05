package com.collabrium.requests.management.application.internal.outboundservices.messaging;

import com.collabrium.requests.management.domain.model.events.RequestCreatedEvent;

public interface RequestsEventPublisher {

  void publishRequestCreated(RequestCreatedEvent event);
}
