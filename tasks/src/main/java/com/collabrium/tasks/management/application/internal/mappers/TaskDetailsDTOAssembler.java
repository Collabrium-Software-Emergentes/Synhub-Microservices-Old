package com.collabrium.tasks.management.application.internal.mappers;

import com.collabrium.tasks.management.application.internal.dto.TaskDetailsDTO;
import com.collabrium.tasks.management.application.internal.dto.TaskMemberDTO;
import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.domain.model.aggregates.Task;
import com.collabrium.tasks.shared.infrastructure.clients.iam.resources.UserOnlyResource;

public class TaskDetailsDTOAssembler {

  private TaskDetailsDTOAssembler() {
  }

  public static TaskDetailsDTO toDTO(
      Task task,
      Member member,
      UserOnlyResource user
  ) {

    var memberDTO =
        new TaskMemberDTO(
            member.getId(),
            user.name(),
            user.surname(),
            user.imgUrl()
        );

    return new TaskDetailsDTO(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getDueDate().toString(),
        task.getCreatedAt().toString(),
        task.getUpdatedAt() != null
            ? task.getUpdatedAt().toString()
            : null,
        task.getStatus().name(),
        task.getTimesRearranged(),
        task.getTimePassed(),
        memberDTO,
        task.getGroupId() != null
            ? task.getGroupId().value()
            : null,
        task.getImageUrl()
    );
  }
}