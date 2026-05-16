package com.collabrium.iam.authentication.interfaces.rest.transform;

import com.collabrium.iam.authentication.domain.model.commands.CreateUserMemberCommand;

public class CreateUserMemberCommandFromResourceAssembler {

  private CreateUserMemberCommandFromResourceAssembler() {
  }

  public static CreateUserMemberCommand toCommandFromResource(
      Long userId
  ) {

    return new CreateUserMemberCommand(
        userId
    );
  }
}
