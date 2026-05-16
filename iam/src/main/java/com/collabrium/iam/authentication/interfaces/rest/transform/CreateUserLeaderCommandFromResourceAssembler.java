package com.collabrium.iam.authentication.interfaces.rest.transform;

import com.collabrium.iam.authentication.domain.model.commands.CreateUserLeaderCommand;

public class CreateUserLeaderCommandFromResourceAssembler {

  private CreateUserLeaderCommandFromResourceAssembler() {
  }

  public static CreateUserLeaderCommand toCommandFromResource(
      Long userId
  ) {

    return new CreateUserLeaderCommand(
        userId
    );
  }
}