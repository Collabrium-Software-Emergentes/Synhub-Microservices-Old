package com.collabrium.groups.management.domain.exceptions;

import com.collabrium.groups.management.domain.model.valueobjects.MemberId;

public class InvitationNotFoundException extends RuntimeException {

  public InvitationNotFoundException(MemberId memberId) {
    super(
        "Invitation not found for member id: " +
            memberId.value()
    );
  }

  public static InvitationNotFoundException forMember(
      MemberId memberId
  ) {

    return new InvitationNotFoundException(memberId);
  }
}
