package com.collabrium.groups.management.application.internal.commandservices;

import com.collabrium.groups.management.application.internal.outboundservices.messaging.GroupsEventPublisher;
import com.collabrium.groups.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.groups.management.application.internal.outboundservices.ports.MediaServicePort;
import com.collabrium.groups.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.groups.management.domain.exceptions.*;
import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.commands.*;
import com.collabrium.groups.management.domain.model.events.GroupCreatedEvent;
import com.collabrium.groups.management.domain.model.events.GroupDeletedEvent;
import com.collabrium.groups.management.domain.model.events.resources.GroupMemberInfo;
import com.collabrium.groups.management.domain.model.events.RemoveMemberEvent;
import com.collabrium.groups.management.domain.model.valueobjects.GroupCode;
import com.collabrium.groups.management.domain.services.GroupCommandService;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.LeaderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for handling group-related commands.
 * Provides business logic for creating, updating, deleting groups,
 * and managing group membership operations.
 *
 * <p>This service orchestrates the entire group management lifecycle,
 * ensuring data consistency and proper event publishing across bounded contexts.</p>
 *
 * @author Collabrium Team
 * @version 1.0
 */
@Service
public class GroupCommandServiceImpl implements GroupCommandService {

  private final GroupRepository groupRepository;
  private final IamQueryPort iamQueryPort;
  private final LeaderRepository leaderRepository;
  private final TasksQueryPort tasksQueryPort;
  private final GroupsEventPublisher groupsEventPublisher;
  private final MediaServicePort mediaServicePort;

  /**
   * Constructs a new GroupCommandServiceImpl with the required dependencies.
   *
   * @param groupRepository the repository for group persistence operations
   * @param iamQueryPort the port for querying user information from IAM service
   * @param leaderRepository the repository for leader persistence operations
   * @param tasksQueryPort the port for querying task-related information
   * @param groupsEventPublisher the publisher for domain events
   * @param mediaServicePort the port for media service operations (image upload/update)
   */
  public GroupCommandServiceImpl(
      GroupRepository groupRepository,
      IamQueryPort iamQueryPort,
      LeaderRepository leaderRepository,
      TasksQueryPort tasksQueryPort,
      GroupsEventPublisher groupsEventPublisher,
      MediaServicePort mediaServicePort
  ) {

    this.groupRepository = groupRepository;
    this.iamQueryPort = iamQueryPort;
    this.leaderRepository = leaderRepository;
    this.tasksQueryPort = tasksQueryPort;
    this.groupsEventPublisher = groupsEventPublisher;
    this.mediaServicePort = mediaServicePort;
  }

  /**
   * Creates a new group for a user with leader privileges.
   *
   * <p><b>Business Rules:</b></p>
   * <ul>
   *   <li>The user must exist in the system</li>
   *   <li>The user must have a valid leader profile</li>
   *   <li>A leader can only own a single group</li>
   *   <li>The group code must be globally unique</li>
   *   <li>The group image is uploaded to the media service</li>
   * </ul>
   *
   * <p><b>Event Published:</b> {@link GroupCreatedEvent} after successful group creation</p>
   *
   * @param command the create a group command containing group details and user ID
   * @return an {@link Optional} containing the created {@link Group} if successful,
   *         or empty if creation fails
   * @throws UserNotFoundException if the user does not exist in the system
   * @throws InvalidLeaderException if the user does not have a leader profile
   * @throws GroupAlreadyExistsException if the leader already owns a group
   * @throws LeaderNotFoundException if the leader profile exists but is not found in the repository
   */
  @Override
  @Transactional
  public Optional<Group> handle(CreateGroupCommand command) {

    var leaderContext = getExistingLeader(command.userId());

    var leader =
        leaderRepository.findById(leaderContext.leaderId())
            .orElseThrow(() ->
                new LeaderNotFoundException(leaderContext.leaderId())
            );

    validateLeaderDoesNotOwnGroup(leaderContext.leaderId());

    GroupCode groupCode = generateUniqueGroupCode();

    var imageResponse = mediaServicePort.uploadGroupImage(command.file());

    var group = new Group(
        command.name(),
        command.description(),
        imageResponse.imageUrl(),
        imageResponse.publicId(),
        leader,
        groupCode
    );

    var savedGroup = groupRepository.save(group);

    var groupCreatedEvent = new GroupCreatedEvent(
        savedGroup.getName(),
        savedGroup.getDescription(),
        savedGroup.getImgUrl().toString(),
        savedGroup.getCode().toString(),
        leaderContext.email
    );

    groupsEventPublisher.publishGroupCreated(groupCreatedEvent);

    return Optional.of(savedGroup);
  }

  /**
   * Updates an existing group's information.
   *
   * <p><b>Business Rules:</b></p>
   * <ul>
   *   <li>The user must exist and be a valid leader</li>
   *   <li>The leader must own a group</li>
   *   <li>If a new image is provided, it will replace the existing one</li>
   *   <li>Only the group owner can update the group information</li>
   * </ul>
   *
   * <p><b>Note:</b> The group image is updated through the media service,
   * which handles the upload and returns the new image URL and public ID.</p>
   *
   * @param command the update group command containing new group information
   * @return an {@link Optional} containing the updated {@link Group}
   * @throws UserNotFoundException if the user does not exist in the system
   * @throws InvalidLeaderException if the user does not have a leader profile
   * @throws GroupNotFoundException if the leader does not own any group
   */
  @Override
  @Transactional
  public Optional<Group> handle(UpdateGroupCommand command) {

    var leaderContext = getExistingLeader(command.userId());

    var group = getLeaderGroup(leaderContext.leaderId());

    group.updateInformation(command);

    if (command.file() != null &&
        !command.file().isEmpty()) {

      var imageResponse =
          mediaServicePort
              .updateGroupImage(
                  command.file(),
                  group.getId()
              );

      group.updateImage(
          imageResponse.imageUrl(),
          imageResponse.publicId()
      );
    }

    var updatedGroup =
        groupRepository.save(group);

    return Optional.of(updatedGroup);
  }

  /**
   * Deletes a group owned by a leader.
   *
   * <p><b>Business Rules:</b></p>
   * <ul>
   *   <li>The user must exist and be a valid leader</li>
   *   <li>The leader must own a group</li>
   *   <li>Only the group owner can delete the group</li>
   * </ul>
   *
   * <p><b>Event Published:</b> {@link GroupDeletedEvent} to notify other services
   * about the group deletion, allowing them to clean up associated resources.</p>
   *
   * <p><b>Note:</b> This operation is transactional and will roll back
   * if any error occurs during the process.</p>
   *
   * @param command the delete group command containing the user ID
   * @throws UserNotFoundException if the user does not exist in the system
   * @throws InvalidLeaderException if the user does not have a leader profile
   * @throws GroupNotFoundException if the leader does not own any group
   */
  @Override
  @Transactional
  public void handle(DeleteGroupCommand command) {

    var leaderContext = getExistingLeader(command.userId());

    var group = getLeaderGroup(leaderContext.leaderId());

    groupRepository.delete(group);

    var usersMembers =
        tasksQueryPort
            .getMembersByGroupId(group.getId())
                .stream()
                    .map(member -> new GroupMemberInfo(
                        member.id(),
                        member.email()
                    ))
                        .toList();

    groupsEventPublisher.publishGroupDeleted(
        new GroupDeletedEvent(
            group.getId(),
            group.getPublicId(),
            group.getName(),
            group.getDescription(),
            group.getCode().toString(),
            leaderContext.email,
            usersMembers
        )
    );
  }

  /**
   * Removes a member from the group owned by the authenticated leader.
   *
   * <p><b>Business Rules:</b></p>
   * <ul>
   *   <li>The leader must exist and own a group</li>
   *   <li>The member must exist in the system</li>
   *   <li>The member must belong to the leader's group</li>
   *   <li>The group's member count is decreased by one</li>
   * </ul>
   *
   * <p><b>Event Published:</b> {@link RemoveMemberEvent} to notify the task service
   * about the member removal, allowing it to update task assignments.</p>
   *
   * @param command the remove member command containing leader's user ID and member ID
   * @throws UserNotFoundException if the leader user does not exist
   * @throws InvalidLeaderException if the user is not a leader
   * @throws GroupNotFoundException if the leader does not own any group
   * @throws MemberNotFoundException if the member does not exist in the system
   * @throws InvalidGroupException if the member does not belong to the leader's group
   */
  @Override
  @Transactional
  public void handle(RemoveMemberFromMyGroupCommand command) {

    var leaderContext = getExistingLeader(command.userId());

    var group = getLeaderGroup(leaderContext.leaderId());

    var member =
        tasksQueryPort.getMemberOnlyById(
            command.memberId()
        );

    if (member == null) {
      throw MemberNotFoundException
          .forMemberId(command.memberId());
    }

    validateMemberBelongsToGroup(
        member.groupId(),
        group.getId(),
        command.memberId()
    );

    group.decreaseMemberCount();

    var memberUserInfo = iamQueryPort.getUserByMemberId(command.memberId());

    groupsEventPublisher.publishMemberRemovedFromGroup(
        new RemoveMemberEvent(
            command.memberId(),
            memberUserInfo.email(),
            group.getName(),
            group.getImgUrl().toString(),
            group.getCode().toString(),
            leaderContext.email
        )
    );
  }

  /**
   * Handles a member voluntarily leaving a group.
   *
   * <p><b>Business Rules:</b></p>
   * <ul>
   *   <li>The group must exist in the system</li>
   *   <li>The member count is decreased by one</li>
   * </ul>
   *
   * <p><b>Note:</b> Unlike {@link #handle(RemoveMemberFromMyGroupCommand)},
   * this method does not validate the member's existence as this is handled
   * at the controller level. This is a self-service operation where the member
   * initiates the leave action.</p>
   *
   * @param command the leave group command containing the group ID
   * @throws GroupNotFoundException if the group does not exist
   */
  @Override
  @Transactional
  public void handle(LeaveGroupCommand command) {

    var group =
        groupRepository
            .findById(command.groupId())
            .orElseThrow(() ->
                GroupNotFoundException.forId(
                    command.groupId()
                )
            );

    group.decreaseMemberCount();
  }

  /**
   * Retrieves an existing leader's context from a user ID.
   *
   * <p>This method performs two validations:</p>
   * <ol>
   *   <li>Verifies that the user exists in the IAM service</li>
   *   <li>Verifies that the user has an associated leader profile</li>
   * </ol>
   *
   * @param userId the user ID to look up
   * @return a {@link LeaderContext} containing the leader ID and email
   * @throws UserNotFoundException if the user does not exist in the system
   * @throws InvalidLeaderException if the user does not have a leader profile
   */
  private LeaderContext getExistingLeader(Long userId) {

    var user = iamQueryPort.getUserOnlyById(userId);

    if (user == null) {
      throw UserNotFoundException.forId(userId);
    }

    if (user.leaderId() == null) {
      throw InvalidLeaderException.forUserIsNotLeader(userId);
    }

    return new LeaderContext(
        user.leaderId(),
        user.email()
    );
  }

  /**
   * Retrieves the group owned by a specific leader.
   *
   * <p>This method assumes the leader exists and is valid.
   * It performs a direct lookup of the group associated with the leader ID.</p>
   *
   * @param leaderId the leader ID whose group to retrieve
   * @return the {@link Group} owned by the leader
   * @throws GroupNotFoundException if no group is found for the leader
   */
  private Group getLeaderGroup(
      Long leaderId
  ) {

    return groupRepository
        .findByLeaderId(leaderId)
        .orElseThrow(() ->
            GroupNotFoundException.forLeader(
                leaderId
            )
        );
  }

  /**
   * Validates that a leader does not already own a group.
   *
   * <p>This method is primarily used during group creation to enforce
   * the business rule that a leader can only own one group.</p>
   *
   * @param leaderId the leader ID to validate
   * @throws GroupAlreadyExistsException if the leader already owns a group
   */
  private void validateLeaderDoesNotOwnGroup(
      Long leaderId
  ) {

    if (
        groupRepository
            .findByLeaderId(leaderId)
            .isPresent()
    ) {

      throw new GroupAlreadyExistsException(
          "Leader already owns a group"
      );
    }
  }

  /**
   * Generates a unique group of code that does not already exist in the repository.
   *
   * <p>This method uses a do-while loop to continuously generate random
   * group codes until a unique one is found. The uniqueness is guaranteed
   * by checking against the existing codes in the repository.</p>
   *
   * <p><b>Performance Note:</b> The probability of collision is extremely low
   * due to the random nature of the code generation, making this approach
   * efficient in practice.</p>
   *
   * @return a unique {@link GroupCode} value object
   */
  private GroupCode generateUniqueGroupCode() {

    GroupCode groupCode;

    do {

      groupCode = GroupCode.generate();

    } while (
        groupRepository.existsByCode(groupCode)
    );

    return groupCode;
  }

  /**
   * Validates that a member belongs to a specific group.
   *
   * <p>This method performs a business rule validation to ensure
   * that a member is correctly associated with a group before
   * performing operations like member removal.</p>
   *
   * @param memberGroupId the group ID associated with the member
   * @param groupId the expected group ID that the member should belong to
   * @param memberId the member ID (used for constructing an exception message)
   * @throws InvalidGroupException if the member's group ID is null or doesn't match
   */
  private void validateMemberBelongsToGroup(
      Long memberGroupId,
      Long groupId,
      Long memberId
  ) {

    if (
        memberGroupId == null ||
            !memberGroupId.equals(groupId)
    ) {

      throw InvalidGroupException
          .forMemberNotBelongingToGroup(
              memberId,
              groupId
          );
    }
  }

  /**
   * Internal record representing the leader context.
   *
   * <p>This record aggregates the leader ID and email for use within
   * the service methods, reducing the need for multiple service calls.</p>
   *
   * @param leaderId the unique identifier of the leader
   * @param email the email address of the leader
   */
  private record LeaderContext(
      Long leaderId,
      String email
  ) {}
}
