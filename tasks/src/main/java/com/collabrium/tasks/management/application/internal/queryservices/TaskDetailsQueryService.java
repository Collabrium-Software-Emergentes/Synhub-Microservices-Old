package com.collabrium.tasks.management.application.internal.queryservices;

import com.collabrium.tasks.management.application.internal.dto.TaskDetailsDTO;
import com.collabrium.tasks.management.application.internal.mappers.TaskDetailsDTOAssembler;
import com.collabrium.tasks.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.tasks.management.domain.exceptions.MemberNotFoundException;
import com.collabrium.tasks.management.domain.exceptions.UserNotFoundException;
import com.collabrium.tasks.management.domain.model.queries.GetAllTasksDetailsByUserIdQuery;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.MemberRepository;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.TaskRepository;
import com.collabrium.tasks.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for handling task details query operations.
 * This service acts as a query handler for retrieving task information
 * enriched with user and member details.
 */
@Service
public class TaskDetailsQueryService {

  private final TaskRepository taskRepository;
  private final MemberRepository memberRepository;
  private final IamQueryPort iamQueryPort;

  /**
   * Constructs a new TaskDetailsQueryService with the required dependencies.
   *
   * @param taskRepository the repository for task data access operations
   * @param memberRepository the repository for member data access operations
   * @param iamQueryPort the port for querying IAM (Identity Access Management) service
   */
  public TaskDetailsQueryService(
      TaskRepository taskRepository,
      MemberRepository memberRepository,
      IamQueryPort iamQueryPort
  ) {

    this.taskRepository = taskRepository;
    this.memberRepository = memberRepository;
    this.iamQueryPort = iamQueryPort;
  }

  /**
   * Handles the query to retrieve all task details for a specific user.
   * This method orchestrates the process of fetching user information from IAM,
   * retrieving the associated member entity, and then getting all tasks
   * belonging to that member.
   *
   * @param query the query containing the user ID for which to retrieve tasks
   * @return a list of TaskDetailsDTO objects containing enriched task information
   *         with member and user details
   * @throws UserNotFoundException if the user with the specified ID does not exist
   * @throws MemberNotFoundException if the user does not have an associated member
   *         profile, or the member cannot be found in the repository
   */
  public List<TaskDetailsDTO> handle(GetAllTasksDetailsByUserIdQuery query) {

    var user =
        iamQueryPort.getUserOnlyById(
            query.userId()
        );

    validateUser(
        query.userId(),
        user
    );

    var member =
        memberRepository
            .findById(user.memberId())
            .orElseThrow(() ->
                MemberNotFoundException.forId(
                    user.memberId()
                )
            );

    var tasks =
        taskRepository.findByMember_Id(
            member.getId()
        );

    return tasks.stream()
        .map(task ->
            TaskDetailsDTOAssembler.toDTO(
                task,
                task.getMember(),
                user
            )
        )
        .toList();
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