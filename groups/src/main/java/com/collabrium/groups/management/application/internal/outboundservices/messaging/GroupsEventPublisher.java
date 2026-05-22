package com.collabrium.groups.management.application.internal.outboundservices.messaging;

import com.collabrium.groups.management.domain.model.events.InvitationAcceptedEvent;
import com.collabrium.groups.management.domain.model.events.LeaderCreatedEvent;

public interface GroupsEventPublisher {

  void publishLeaderCreated(LeaderCreatedEvent event);

  void publishInvitationAccepted(InvitationAcceptedEvent event);
}
