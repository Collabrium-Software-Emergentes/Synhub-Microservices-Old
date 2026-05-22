package com.collabrium.groups.management.application.internal.commandservices;

import com.collabrium.groups.management.application.internal.ports.IamQueryPort;
import com.collabrium.groups.management.domain.exceptions.InvitationNotFoundException;
import com.collabrium.groups.management.domain.exceptions.MemberNotFoundException;
import com.collabrium.groups.management.domain.exceptions.UserNotFoundException;
import com.collabrium.groups.management.domain.model.commands.AcceptInvitationCommand;
import com.collabrium.groups.management.domain.model.commands.CancelInvitationCommand;
import com.collabrium.groups.management.domain.model.commands.RejectInvitationCommand;
import com.collabrium.groups.management.domain.model.valueobjects.MemberId;
import com.collabrium.groups.management.domain.services.InvitationCommandService;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.InvitationRepository;
import org.springframework.stereotype.Service;

@Service
public class InvitationCommandServiceImpl implements InvitationCommandService {

  private final InvitationRepository invitationRepository;
  private final IamQueryPort iamQueryPort;

  public InvitationCommandServiceImpl(
      InvitationRepository invitationRepository, IamQueryPort iamQueryPort
  ) {

    this.invitationRepository = invitationRepository;
    this.iamQueryPort = iamQueryPort;
  }

  @Override
  public void handle(RejectInvitationCommand command) {

  }

  @Override
  public void handle(CancelInvitationCommand command) {

    var user = iamQueryPort.getUserOnlyById(command.userId());

    if (user == null) {
      throw new UserNotFoundException(command.userId());
    }

    if (user.memberId() == null) {
      throw new MemberNotFoundException(command.userId());
    }

    MemberId memberId = MemberId.of(user.memberId());

    var invitation = invitationRepository
        .findByMemberId(memberId)
        .orElseThrow(() ->
            new InvitationNotFoundException(memberId)
        );

    invitationRepository.delete(invitation);
  }

  @Override
  public void handle(AcceptInvitationCommand command) {

  }
}
