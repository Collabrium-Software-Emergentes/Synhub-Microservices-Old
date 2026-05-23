package com.collabrium.tasks.management.domain.services;

import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.domain.model.commands.AssignMemberToGroupCommand;
import com.collabrium.tasks.management.domain.model.commands.CreateMemberCommand;
import com.collabrium.tasks.management.domain.model.commands.DeleteMembersByGroupIdCommand;
import com.collabrium.tasks.management.domain.model.commands.RemoveMemberFromGroupCommand;

import java.util.Optional;

public interface MemberCommandService {

  void handle(CreateMemberCommand command);

  Optional<Member> handle(AssignMemberToGroupCommand command);

  Optional<Member> handle(RemoveMemberFromGroupCommand command);

  void handle(DeleteMembersByGroupIdCommand command);
}
