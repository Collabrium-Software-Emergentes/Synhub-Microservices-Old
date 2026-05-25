package com.collabrium.tasks.management.domain.services;

import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface MemberQueryService {

  /**
   * Retrieves a member by their ID.
   *
   * @param query the query containing the member ID
   * @return an Optional containing the member if found, or empty if not found
   */
  Optional<Member> handle(GetMemberByIdQuery query);

  /**
   * Retrieves all members.
   *
   * @return a list of all members
   */
  List<Member> handle(GetAllMembersQuery query);

  List<Member> handle(GetAllMembersByGroupIdQuery query);
}