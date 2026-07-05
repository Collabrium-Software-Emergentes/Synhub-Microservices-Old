package com.collabrium.tasks.management.application.internal.commandservices;

import com.collabrium.tasks.management.application.internal.dto.TaskDetailsDTO;
import com.collabrium.tasks.management.application.internal.mappers.TaskDetailsDTOAssembler;
import com.collabrium.tasks.management.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.tasks.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.tasks.management.application.internal.outboundservices.ports.MediaServicePort;
import com.collabrium.tasks.management.domain.exceptions.InvalidTaskException;
import com.collabrium.tasks.management.domain.exceptions.MemberNotFoundException;
import com.collabrium.tasks.management.domain.exceptions.UserNotFoundException;
import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.domain.model.aggregates.Task;
import com.collabrium.tasks.management.domain.model.commands.CreateTaskCommand;
import com.collabrium.tasks.management.domain.model.commands.UpdateTaskCommand;
import com.collabrium.tasks.management.domain.model.commands.UpdateTaskStatusCommand;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.MemberRepository;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.TaskRepository;
import com.collabrium.tasks.shared.infrastructure.clients.groups.resources.GroupOnlyResource;
import com.collabrium.tasks.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class responsible for handling task command operations.
 * This service orchestrates the process of creating and updating tasks by validating
 * business rules, ensuring proper authorization, and integrating with
 * external services for user, member, and group information.
 *
 * @author Collabrium Team
 * @version 1.0
 */
@Service
public class TaskDetailsCommandService {

  private final TaskRepository taskRepository;
  private final MemberRepository memberRepository;
  private final IamQueryPort iamQueryPort;
  private final GroupsQueryPort groupsQueryPort;
  private final MediaServicePort mediaServicePort;
  /**
   * Constructs a new TaskDetailsCommandService with the required dependencies.
   *
   * @param taskRepository the repository for task persistence operations
   * @param memberRepository the repository for member data access operations
   * @param iamQueryPort the port for querying IAM (Identity Access Management) service
   * @param groupsQueryPort the port for querying Groups management service
   */
  public TaskDetailsCommandService(
          TaskRepository taskRepository,
          MemberRepository memberRepository,
          IamQueryPort iamQueryPort,
          GroupsQueryPort groupsQueryPort,
          MediaServicePort mediaServicePort
  ) {

    this.taskRepository = taskRepository;
    this.memberRepository = memberRepository;
    this.iamQueryPort = iamQueryPort;
    this.groupsQueryPort = groupsQueryPort;
    this.mediaServicePort = mediaServicePort;
  }

  /**
   * Handles the command to create a new task.
   * This method performs comprehensive validation including:
   * <ul>
   *   <li>Verifying the leader user exists and has leader privileges</li>
   *   <li>Ensuring the target member exists</li>
   *   <li>Validating the member belongs to a group</li>
   *   <li>Confirming the associated group exists</li>
   *   <li>Validating the leader belongs to the same group as the member</li>
   *   <li>Retrieving member user details for the response DTO</li>
   * </ul>
   *
   * @param command the command containing task creation data including userId,
   *                memberId, and task details
   * @return an Optional containing the TaskDetailsDTO of the created task
   * @throws UserNotFoundException if the leader user does not exist
   * @throws InvalidTaskException if the user is not a leader, the member has no group,
   *         a group doesn't exist, or the leader doesn't belong to the group
   * @throws MemberNotFoundException if the target member does not exist
   */
  public Optional<TaskDetailsDTO> handle(CreateTaskCommand command) {

    var leaderUser =
            getExistingUser(command.userId());

    validateLeaderUser(leaderUser);

    var member =
            getExistingMember(command.memberId());

    validateMemberGroup(member);

    var group =
            validateGroupExists(member);

    validateLeaderBelongsToGroup(
            leaderUser,
            group
    );

    var task = new Task(command);

    task.setMember(member);
    task.setGroupId(member.getGroupId());

    if (command.file() != null &&
            !command.file().isEmpty()) {

      var imageResponse =
              mediaServicePort.uploadTaskImage(
                      command.file()
              );

      task.updateImage(
              imageResponse.imageUrl(),
              imageResponse.publicId()
      );
    }

    var savedTask =
            taskRepository.save(task);

    return Optional.of(
            buildTaskDetailsDTO(savedTask)
    );
  }

  /**
   * Handles the command to update a task's status.
   * This method validates that the user requesting the update belongs to the
   * same group as the task before allowing the status change.
   *
   * @param command the command containing task ID, user ID, and new status
   * @return an Optional containing the updated TaskDetailsDTO
   * @throws InvalidTaskException if the task is not found, the task has no group ID,
   *         the user has no role, or the user doesn't belong to the task's group
   * @throws UserNotFoundException if the user does not exist
   * @throws MemberNotFoundException if the user has a member ID but the member doesn't exist
   */
  public Optional<TaskDetailsDTO> handle(UpdateTaskStatusCommand command) {

    var task =
        getExistingTask(command.taskId());

    var user =
        getExistingUser(command.userId());

    validateUserBelongsToTaskGroup(
        user,
        getTaskGroupId(task)
    );

    task.updateStatus(command);

    var updatedTask =
        taskRepository.save(task);

    return Optional.of(
        buildTaskDetailsDTO(updatedTask)
    );
  }



  @Transactional
  public Optional<TaskDetailsDTO> handle(UpdateTaskCommand command) {

    if (command == null) {
      throw InvalidTaskException.forNullUpdateCommand();
    }

    var task =
            getExistingTask(command.taskId());

    var taskGroupId =
            getTaskGroupId(task);

    var user =
            getExistingUser(command.userId());

    validateUserBelongsToTaskGroup(
            user,
            taskGroupId
    );

    var newMember =
            getExistingMember(command.memberId());

    validateMemberGroup(newMember);

    if (
            !newMember.getGroupId()
                    .value()
                    .equals(taskGroupId)
    ) {

      throw InvalidTaskException
              .forMemberNotBelongingToGroup(
                      newMember.getId(),
                      taskGroupId
              );
    }

    var currentMember =
            task.getMember();

    if (
            currentMember == null ||
                    !currentMember.getId()
                            .equals(newMember.getId())
    ) {

      task.setMember(newMember);
      task.setGroupId(
              newMember.getGroupId()
      );
    }

    task.updateTask(command);

    if (command.file() != null &&
            !command.file().isEmpty()) {

      var imageResponse =
              mediaServicePort.updateTaskImage(
                      command.file(),
                      task.getId()
              );

      task.updateImage(
              imageResponse.imageUrl(),
              imageResponse.publicId()
      );
    }

    var updatedTask =
            taskRepository.save(task);

    return Optional.of(
            buildTaskDetailsDTO(updatedTask)
    );
  }

  /**
   * Validates that a user has leader privileges.
   * This method ensures the user has a valid leader ID assigned to their profile.
   *
   * @param user the UserOnlyResource object retrieved from IAM
   * @throws InvalidTaskException if the user does not have a leader ID
   *         (i.e., the user is not a leader)
   */
  private void validateLeaderUser(
      UserOnlyResource user
  ) {

    if (user.leaderId() == null) {
      throw InvalidTaskException
          .forUserIsNotLeader(user.id());
    }
  }

  /**
   * Validates that a member belongs to a group.
   * Tasks can only be assigned to members who are part of a group.
   *
   * @param member the Member entity to validate
   * @throws InvalidTaskException if the member does not have an associated group ID
   */
  private void validateMemberGroup(
      Member member
  ) {

    if (member.getGroupId() == null) {
      throw InvalidTaskException.forMemberWithoutGroup(
          member.getId()
      );
    }
  }

  /**
   * Validates that the group associated with a member exists in the group service.
   * This method fetches the group information from the external groups service
   * and verifies its existence.
   *
   * @param member the Member entity whose group to validate
   * @return the GroupOnlyResource containing the group information
   * @throws InvalidTaskException if the group does not exist in the group service
   */
  private GroupOnlyResource validateGroupExists(
      Member member
  ) {

    var groupId =
        member.getGroupId().value();

    var group =
        groupsQueryPort.getGroupOnlyById(
            groupId
        );

    if (group == null) {
      throw InvalidTaskException.forGroupNotFound(
          groupId
      );
    }

    return group;
  }

  /**
   * Validates that the leader belongs to the same group as the member.
   * This ensures that a leader can only create tasks for members within
   * their own group, maintaining proper authorization boundaries.
   *
   * @param leaderUser the UserOnlyResource containing the leader's information
   * @param group the GroupOnlyResource containing the group information
   * @throws InvalidTaskException if the group has no leader assigned, or if the
   *         leader's ID does not match the group's leader ID
   */
  private void validateLeaderBelongsToGroup(
      UserOnlyResource leaderUser,
      GroupOnlyResource group
  ) {

    if (
        group.leaderId() == null ||
            !group.leaderId().equals(
                leaderUser.leaderId()
            )
    ) {

      throw InvalidTaskException
          .forLeaderNotBelongingToGroup(
              leaderUser.id(),
              group.id()
          );
    }
  }

  /**
   * Validates that a user belongs to the same group as a task.
   * This method determines the user's role (leader or member) and retrieves
   * their associated group ID, then verifies it matches the task's group ID.
   *
   * @param user the UserOnlyResource containing the user's information
   * @param taskGroupId the ID of the group associated with the task
   * @throws InvalidTaskException if the user has no role, the leader has no group,
   *         the member has no group, or the user doesn't belong to the task's group
   * @throws MemberNotFoundException if the user has a member ID but the member doesn't exist
   */
  private void validateUserBelongsToTaskGroup(
      UserOnlyResource user,
      Long taskGroupId
  ) {

    Long userGroupId;

    // =====================================================
    // LEADER
    // =====================================================

    if (user.leaderId() != null) {

      var group =
          groupsQueryPort.getGroupByLeaderId(
              user.leaderId()
          );

      if (group == null) {
        throw InvalidTaskException
            .forLeaderWithoutGroup(
                user.id()
            );
      }

      userGroupId = group.id();
    }

    // =====================================================
    // MEMBER
    // =====================================================

    else if (user.memberId() != null) {

      var member =
          memberRepository
              .findById(user.memberId())
              .orElseThrow(() ->
                  MemberNotFoundException.forId(
                      user.memberId()
                  )
              );

      if (member.getGroupId() == null) {
        throw InvalidTaskException
            .forMemberWithoutGroup(
                member.getId()
            );
      }

      userGroupId =
          member.getGroupId().value();
    }

    // =====================================================
    // NO ROLE
    // =====================================================

    else {

      throw InvalidTaskException
          .forUserWithoutRole(
              user.id()
          );
    }

    // =====================================================
    // FINAL VALIDATION
    // =====================================================

    if (!userGroupId.equals(taskGroupId)) {

      throw InvalidTaskException
          .forUserNotBelongingToGroup(
              user.id(),
              taskGroupId
          );
    }
  }

  /**
   * Retrieves the user associated with a specific member ID.
   *
   * @param memberId the ID of the member whose user information to retrieve
   * @return the UserOnlyResource containing the user's information
   * @throws UserNotFoundException if no user is found for the given member ID
   */
  private UserOnlyResource getMemberUser(
      Long memberId
  ) {

    var user =
        iamQueryPort.getUserByMemberId(
            memberId
        );

    if (user == null) {
      throw UserNotFoundException
          .forMember(memberId);
    }

    return user;
  }

  /**
   * Retrieves the member associated with a task.
   *
   * @param task the Task entity from which to extract the member
   * @return the Member entity associated with the task
   * @throws InvalidTaskException if the task has no associated member
   */
  private Member getTaskMember(
      Task task
  ) {

    var member = task.getMember();

    if (member == null) {
      throw InvalidTaskException
          .forNullMember();
    }

    return member;
  }

  /**
   * Retrieves an existing task by its ID.
   *
   * @param taskId the ID of the task to retrieve
   * @return the Task entity if found
   * @throws InvalidTaskException if no task exists with the given ID
   */
  private Task getExistingTask(
      Long taskId
  ) {

    return taskRepository
        .findById(taskId)
        .orElseThrow(() ->
            InvalidTaskException
                .forTaskNotFound(taskId)
        );
  }

  /**
   * Retrieves an existing member by their ID.
   *
   * @param memberId the ID of the member to retrieve
   * @return the Member entity if found
   * @throws MemberNotFoundException if no member exists with the given ID
   */
  private Member getExistingMember(
      Long memberId
  ) {

    return memberRepository
        .findById(memberId)
        .orElseThrow(() ->
            MemberNotFoundException
                .forId(memberId)
        );
  }

  /**
   * Retrieves an existing user by their ID from the IAM service.
   *
   * @param userId the ID of the user to retrieve
   * @return the UserOnlyResource containing the user's information
   * @throws UserNotFoundException if no user exists with the given ID
   */
  private UserOnlyResource getExistingUser(
      Long userId
  ) {

    var user =
        iamQueryPort.getUserOnlyById(
            userId
        );

    if (user == null) {
      throw UserNotFoundException
          .forId(userId);
    }

    return user;
  }

  /**
   * Retrieves the group ID associated with a task.
   *
   * @param task the Task entity from which to extract the group ID
   * @return the Long value of the group ID
   * @throws InvalidTaskException if the task has no associated group ID
   */
  private Long getTaskGroupId(
      Task task
  ) {

    var groupId =
        task.getGroupId();

    if (groupId == null) {
      throw InvalidTaskException
          .forNullGroupId();
    }

    return groupId.value();
  }

  /**
   * Builds a TaskDetailsDTO from a Task entity.
   * This method retrieves the associated member and user information
   * to create a complete DTO for API responses.
   *
   * @param task the Task entity to convert
   * @return the TaskDetailsDTO containing enriched task information
   * @throws InvalidTaskException if the task has no associated member or group ID
   * @throws UserNotFoundException if the member has no associated user
   * @throws MemberNotFoundException if the member cannot be found
   */
  private TaskDetailsDTO buildTaskDetailsDTO(
      Task task
  ) {

    var member =
        getTaskMember(task);

    var memberUser =
        getMemberUser(member.getId());

    return TaskDetailsDTOAssembler.toDTO(
        task,
        member,
        memberUser
    );
  }
}
