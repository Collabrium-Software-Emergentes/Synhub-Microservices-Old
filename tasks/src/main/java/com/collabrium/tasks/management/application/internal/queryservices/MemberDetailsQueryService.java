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

/**
 * Service class responsible for handling member details query operations.
 * This service orchestrates the retrieval of member information by integrating
 * data from multiple sources including local member repository, IAM service,
 * and groups management service.
 */
@Service
public class MemberDetailsQueryService {

  private final MemberRepository memberRepository;
  private final IamQueryPort iamQueryPort;
  private final GroupsQueryPort groupsQueryPort;

  /**
   * Constructs a new MemberDetailsQueryService with the required dependencies.
   *
   * @param memberRepository the repository for member data access operations
   * @param iamQueryPort the port for querying IAM (Identity Access Management) service
   * @param groupsQueryPort the port for querying Groups management service
   */
  public MemberDetailsQueryService(
      MemberRepository memberRepository,
      IamQueryPort iamQueryPort,
      GroupsQueryPort groupsQueryPort
  ) {

    this.memberRepository = memberRepository;
    this.iamQueryPort = iamQueryPort;
    this.groupsQueryPort = groupsQueryPort;
  }

  /**
   * Handles the query to retrieve member details by member ID.
   * This method fetches member information from the local repository and
   * enriches it with user data from the IAM service.
   *
   * @param query the query containing the member ID to retrieve
   * @return an Optional containing the MemberDetailsDTO if found
   * @throws MemberNotFoundException if no member exists with the specified ID
   * @throws UserNotFoundException if no user is associated with the member
   */
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

  /**
   * Handles the query to retrieve member details by user ID.
   * This method first fetches user information from IAM, then retrieves
   * the associated member and combines both into a detailed DTO.
   *
   * @param query the query containing the user ID to retrieve member details for
   * @return an Optional containing the MemberDetailsDTO if found
   * @throws UserNotFoundException if the user does not exist or has no member association
   * @throws MemberNotFoundException if the member associated with the user cannot be found
   */
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

  /**
   * Handles the query to retrieve extended group information for a user.
   * This method fetches the user's member record, retrieves the associated
   * group details, and enriches it with all member information within that group.
   *
   * @param query the query containing the user ID to retrieve group information for
   * @return an Optional containing the ExtendedGroupDTO if the user belongs to a group,
   *         or empty Optional if the user is not a member of any group
   * @throws UserNotFoundException if the user does not exist or has no member association
   * @throws MemberNotFoundException if the member associated with the user cannot be found
   */
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

  /**
   * Handles the query to retrieve all member details for a specific group.
   * This method fetches all members belonging to the specified group and
   * enriches each member with user details from the IAM service.
   *
   * @param query the query containing the group ID to retrieve members for
   * @return a List of MemberDetailsDTO objects containing detailed information
   *         for all members in the specified group
   */
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

  /**
   * Builds a MemberDetailsDTO from a Member entity.
   * This method fetches the associated user information from IAM and
   * combines it with the member data to create a complete DTO.
   *
   * @param member the Member entity to build the DTO from
   * @return a MemberDetailsDTO containing the combined member and user information
   * @throws UserNotFoundException if no user is found for the member ID
   */
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

  /**
   * Validates the existence and completeness of a user resource.
   * This method ensures that the user exists in the IAM system and that
   * the user has a valid member ID associated with their profile.
   *
   * @param userId the ID of the user being validated
   * @param user the UserOnlyResource object retrieved from IAM
   * @throws UserNotFoundException if the user resource is null (user not found)
   * @throws MemberNotFoundException if the user resource exists but has a null memberId
   */
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