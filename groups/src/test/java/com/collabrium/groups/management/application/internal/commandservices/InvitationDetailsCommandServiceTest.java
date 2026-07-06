package com.collabrium.groups.management.application.internal.commandservices;

import com.collabrium.groups.management.application.internal.dto.InvitationDetailsDTO;
import com.collabrium.groups.management.application.internal.outboundservices.messaging.GroupsEventPublisher;
import com.collabrium.groups.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.aggregates.Invitation;
import com.collabrium.groups.management.domain.model.aggregates.Leader;
import com.collabrium.groups.management.domain.model.commands.CreateInvitationCommand;
import com.collabrium.groups.management.domain.model.events.InvitationCreatedEvent;
import com.collabrium.groups.management.domain.model.valueobjects.GroupCode;
import com.collabrium.groups.management.domain.model.valueobjects.MemberId;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.InvitationRepository;
import com.collabrium.groups.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvitationDetailsCommandServiceTest {

  @Mock
  InvitationRepository invitationRepository;

  @Mock
  GroupRepository groupRepository;

  @Mock
  IamQueryPort iamQueryPort;

  @Mock
  GroupsEventPublisher groupsEventPublisher;

  @InjectMocks
  InvitationDetailsCommandService service;

  @Test
  @DisplayName("handle(CreateInvitationCommand) - valid command: creates invitation and returns details")
  void handle_createInvitationCommand_validCommand_createsInvitationAndReturnsDetails() {

    // Arrange
    var command = new CreateInvitationCommand(10L, 20L);

    var leader = mock(Leader.class);
    when(leader.getId()).thenReturn(5L);

    var group = mock(Group.class);
    when(group.getId()).thenReturn(20L);
    when(group.getName()).thenReturn("Backend Team");
    when(group.getDescription()).thenReturn("Description");
    when(group.getMemberCount()).thenReturn(3);
    when(group.getLeader()).thenReturn(leader);

    var code = mock(GroupCode.class);
    when(code.value()).thenReturn("ABC123456");
    when(group.getCode()).thenReturn(code);

    when(group.getImgUrl()).thenReturn(null);

    var user = new UserOnlyResource(
        "jdoe",
        "John",
        "Doe",
        "image.jpg",
        "john@test.com",
        null,
        100L
    );

    var leaderUser = new UserOnlyResource(
        "leader",
        "Leader",
        "User",
        null,
        "leader@test.com",
        null,
        null
    );

    var savedInvitation = mock(Invitation.class);
    when(savedInvitation.getId()).thenReturn(1L);
    when(savedInvitation.getGroup()).thenReturn(group);

    when(groupRepository.findById(20L))
        .thenReturn(Optional.of(group));

    when(iamQueryPort.getUserOnlyById(10L))
        .thenReturn(user);

    when(iamQueryPort.getUserByLeaderId(5L))
        .thenReturn(leaderUser);

    when(invitationRepository.existsByMemberId(MemberId.of(100L)))
        .thenReturn(false);

    when(invitationRepository.save(any(Invitation.class)))
        .thenReturn(savedInvitation);

    // Act
    var result = service.handle(command);

    // Assert
    assertThat(result).isPresent();

    InvitationDetailsDTO dto = result.get();

    assertThat(dto.id()).isEqualTo(1L);
    assertThat(dto.userId()).isEqualTo(10L);
    assertThat(dto.username()).isEqualTo("jdoe");
    assertThat(dto.groupId()).isEqualTo(20L);
    assertThat(dto.groupName()).isEqualTo("Backend Team");

    var eventCaptor =
        ArgumentCaptor.forClass(InvitationCreatedEvent.class);

    verify(groupsEventPublisher)
        .publishInvitationCreated(eventCaptor.capture());

    var event = eventCaptor.getValue();

    assertThat(event.leaderEmail())
        .isEqualTo("leader@test.com");

    assertThat(event.memberUsername())
        .isEqualTo("jdoe");
  }
}