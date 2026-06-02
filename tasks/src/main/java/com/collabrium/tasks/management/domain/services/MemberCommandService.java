package com.collabrium.tasks.management.domain.services;

import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.domain.model.commands.*;

import java.util.Optional;

public interface MemberCommandService {

  void handle(CreateMemberCommand command);

  Optional<Member> handle(AssignMemberToGroupCommand command);

  Optional<Member> handle(RemoveMemberFromGroupCommand command);

  void handle(LeaveGroupCommand command);

  void handle(DeleteGroupDataCommand command);
}
