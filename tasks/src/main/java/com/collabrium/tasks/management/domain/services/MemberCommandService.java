package com.collabrium.tasks.management.domain.services;

import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.domain.model.commands.AddGroupToMemberCommand;
import com.collabrium.tasks.management.domain.model.commands.CreateMemberCommand;
import com.collabrium.tasks.management.domain.model.commands.DeleteMembersByGroupIdCommand;
import com.collabrium.tasks.management.domain.model.commands.RemoveMemberFromGroupCommand;

import java.util.Optional;

public interface MemberCommandService {

  Optional<Member> handle(CreateMemberCommand command);

  Optional<Member> handle(AddGroupToMemberCommand command);

  Optional<Member> handle(RemoveMemberFromGroupCommand command);

  void handle(DeleteMembersByGroupIdCommand command);
}
