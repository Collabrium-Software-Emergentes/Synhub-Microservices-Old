package com.collabrium.tasks.management.application.internal.queryservices;

import com.collabrium.tasks.management.application.internal.dto.ExtendedGroupDTO;
import com.collabrium.tasks.management.application.internal.dto.MemberDetailsDTO;
import com.collabrium.tasks.management.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.tasks.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.tasks.management.domain.exceptions.MemberNotFoundException;
import com.collabrium.tasks.management.domain.exceptions.UserNotFoundException;
import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.domain.model.queries.GetExtendedGroupByUserIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetMemberDetailsByIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetMemberDetailsByUserIdQuery;
import com.collabrium.tasks.management.domain.model.queries.GetMembersDetailsByGroupIdQuery;
import com.collabrium.tasks.management.domain.model.valueobjects.GroupId;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.MemberRepository;
import com.collabrium.tasks.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberDetailsQueryService {

  private final MemberRepository memberRepository;
  private final IamQueryPort iamQueryPort;
  private final GroupsQueryPort groupsQueryPort;

  public MemberDetailsQueryService(
      MemberRepository memberRepository,
      IamQueryPort iamQueryPort,
      GroupsQueryPort groupsQueryPort
  ) {

    this.memberRepository = memberRepository;
    this.iamQueryPort = iamQueryPort;
    this.groupsQueryPort = groupsQueryPort;
  }

  public Optional<MemberDetailsDTO> handle(GetMemberDetailsByIdQuery query) {

    var member = memberRepository
        .findById(query.memberId())
        .orElseThrow(() ->
            MemberNotFoundException.forId(query.memberId())
        );

    var user = iamQueryPort.getUserByMemberId(query.memberId());

    if (user == null) {
      throw UserNotFoundException.forMember(query.memberId());
    }

    var memberDetailsDTO = new MemberDetailsDTO(
        member.getId(),
        user.username(),
        user.name(),
        user.surname(),
        user.imgUrl(),
        user.email(),
        member.getGroupId() != null
            ? member.getGroupId().value()
            : null
    );

    return Optional.of(memberDetailsDTO);
  }

  public Optional<MemberDetailsDTO> handle(GetMemberDetailsByUserIdQuery query) {

    var user = iamQueryPort.getUserOnlyById(query.userId());

    validateUser(query.userId(), user);

    var member = memberRepository
        .findById(user.memberId())
        .orElseThrow(() ->
            MemberNotFoundException.forId(user.memberId())
        );

    var memberDetailsDTO
        = new MemberDetailsDTO(
            member.getId(),
            user.username(),
            user.name(),
            user.surname(),
            user.imgUrl(),
            user.email(),
            member.getGroupId() != null
                ? member.getGroupId().value()
                : null
    );

    return Optional.of(memberDetailsDTO);
  }

  public Optional<ExtendedGroupDTO> handle(GetExtendedGroupByUserIdQuery query) {

    var user = iamQueryPort.getUserOnlyById(query.userId());

    validateUser(query.userId(), user);

    var member =
        memberRepository
            .findById(user.memberId())
            .orElseThrow(() ->
                MemberNotFoundException.forId(
                    user.memberId()
                )
            );

    if (member.getGroupId() == null) {
      return Optional.empty();
    }

    var groupId = member.getGroupId().value();

    var group = groupsQueryPort.getGroupOnlyById(groupId);

    var members =
        memberRepository.findMembersByGroupId(
            member.getGroupId()
        );

    var memberDetails =
        members.stream()
            .map(this::buildMemberDetails)
            .toList();

    var dto =
        new ExtendedGroupDTO(
            group.id(),
            group.name(),
            group.imgUrl(),
            group.description(),
            group.code(),
            memberDetails
        );

    return Optional.of(dto);
  }

  public List<MemberDetailsDTO> handle(
      GetMembersDetailsByGroupIdQuery query
  ) {

    var members =
        memberRepository.findMembersByGroupId(
            new GroupId(query.groupId())
        );

    return members.stream()
        .map(this::buildMemberDetails)
        .toList();
  }

  private MemberDetailsDTO buildMemberDetails(
      Member member
  ) {

    var user =
        iamQueryPort.getUserByMemberId(
            member.getId()
        );

    if (user == null) {
      throw UserNotFoundException.forMember(
          member.getId()
      );
    }

    return new MemberDetailsDTO(
        member.getId(),
        user.username(),
        user.name(),
        user.surname(),
        user.imgUrl(),
        user.email(),
        member.getGroupId() != null
            ? member.getGroupId().value()
            : null
    );
  }

  private void validateUser(
      Long userId,
      UserOnlyResource user
  ) {

    if (user == null) {
      throw UserNotFoundException.forId(userId);
    }

    if (user.memberId() == null) {
      throw MemberNotFoundException.forUser(userId);
    }
  }
}