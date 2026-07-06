package com.collabrium.groups.management.application.internal.commandservices;

import com.collabrium.groups.management.application.internal.outboundservices.messaging.GroupsEventPublisher;
import com.collabrium.groups.management.domain.model.aggregates.Leader;
import com.collabrium.groups.management.domain.model.commands.CreateLeaderCommand;
import com.collabrium.groups.management.domain.model.events.LeaderCreatedEvent;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.LeaderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaderCommandServiceImplTest {

  @Mock
  LeaderRepository leaderRepository;

  @Mock
  GroupsEventPublisher groupsEventPublisher;

  @InjectMocks
  LeaderCommandServiceImpl service;

  @Test
  @DisplayName("handle(CreateLeaderCommand) - valid command: saves leader and publishes event")
  void handle_createLeaderCommand_validCommand_savesLeaderAndPublishesEvent() {
    // Arrange
    var command = new CreateLeaderCommand(10L);

    var savedLeader = mock(Leader.class);

    when(savedLeader.getId()).thenReturn(1L);
    when(leaderRepository.save(any(Leader.class)))
        .thenReturn(savedLeader);

    // Act
    service.handle(command);

    // Assert
    verify(leaderRepository).save(any(Leader.class));

    var eventCaptor =
        ArgumentCaptor.forClass(LeaderCreatedEvent.class);

    verify(groupsEventPublisher)
        .publishLeaderCreated(eventCaptor.capture());

    var publishedEvent = eventCaptor.getValue();

    assertThat(publishedEvent.userId())
        .isEqualTo(10L);

    assertThat(publishedEvent.leaderId())
        .isEqualTo(1L);
  }
}