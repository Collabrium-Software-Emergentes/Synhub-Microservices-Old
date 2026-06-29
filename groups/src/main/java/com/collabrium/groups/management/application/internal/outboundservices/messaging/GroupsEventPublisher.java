package com.collabrium.groups.management.application.internal.outboundservices.messaging;

import com.collabrium.groups.management.domain.model.events.*;

public interface GroupsEventPublisher {

  void publishLeaderCreated(LeaderCreatedEvent event);

  void publishInvitationAccepted(InvitationAcceptedEvent event);

  void publishMemberRemovedFromGroup(RemoveMemberEvent event);

  void publishGroupDeleted(GroupDeletedEvent event);

  void publishGroupCreated(GroupCreatedEvent event);
}