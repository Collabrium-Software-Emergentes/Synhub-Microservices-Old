package com.collabrium.tasks.management.application.internal.queryservices;

import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.domain.model.queries.GetAllMembersByGroupIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetMemberByIdQuery;
import com.collabrium.tasks.management.domain.model.valueobjects.GroupId;
import com.collabrium.tasks.management.domain.services.MemberQueryService;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberQueryServiceImpl implements MemberQueryService {

  private final MemberRepository memberRepository;

  public MemberQueryServiceImpl(
      MemberRepository memberRepository
  ) {

    this.memberRepository = memberRepository;
  }

  @Override
  public Optional<Member> handle(GetMemberByIdQuery query) {
    return memberRepository.findById(query.memberId());
  }

  @Override
  public List<Member> handle(GetAllMembersByGroupIdQuery query) {

    var groupId = new GroupId(query.groupId());

    return memberRepository.findMembersByGroupId(groupId);
  }
}