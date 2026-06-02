package com.collabrium.metrics.management.application.internal.assemblers;

import com.collabrium.metrics.management.application.internal.dto.MemberTaskInfoDTO;
import com.collabrium.metrics.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskDistributionDetailsAssembler {

  private TaskDistributionDetailsAssembler() {
  }

  public static Map<String, MemberTaskInfoDTO> fromTasks(
      List<TaskOnlyResource> tasks,
      Function<Long, String> memberNameResolver
  ) {

    return tasks.stream()
        .filter(task -> task.memberId() != null)
        .collect(
            Collectors.groupingBy(
                TaskOnlyResource::memberId
            )
        )
        .entrySet()
        .stream()
        .collect(
            Collectors.toMap(
                entry -> entry.getKey().toString(),
                entry -> new MemberTaskInfoDTO(
                    memberNameResolver.apply(
                        entry.getKey()
                    ),
                    entry.getValue().size()
                )
            )
        );
  }
}