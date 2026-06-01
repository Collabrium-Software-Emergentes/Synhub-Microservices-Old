package com.collabrium.metrics.management.application.internal.queryservices;

import com.collabrium.metrics.management.application.internal.dto.*;
import com.collabrium.metrics.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.metrics.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.metrics.management.domain.model.queries.GetAvgCompletionTimeForMemberQuery;
import com.collabrium.metrics.management.domain.model.queries.GetRescheduledTasksForMemberQuery;
import com.collabrium.metrics.management.domain.model.queries.GetTaskDistributionForMemberQuery;
import com.collabrium.metrics.management.domain.model.queries.GetTaskOverviewForMemberQuery;
import com.collabrium.metrics.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MetricsQueryService {

  private static final String TASK_STATUS = "DONE";

  private final TasksQueryPort tasksQueryPort;
  private final IamQueryPort iamQueryPort;

  public MetricsQueryService(
      TasksQueryPort tasksQueryPort,
      IamQueryPort iamQueryPort
  ) {

    this.tasksQueryPort = tasksQueryPort;
    this.iamQueryPort = iamQueryPort;
  }

  public Optional<AvgCompletionTimeDTO> handle(
      GetAvgCompletionTimeForMemberQuery query
  ) {

    var tasks =
        tasksQueryPort.getTasksByMemberId(
            query.memberId()
        );

    var completedTasks =
        tasks.stream()
            .filter(task ->
                TASK_STATUS.equals(
                    task.status()
                )
            )
            .toList();

    if (completedTasks.isEmpty()) {
      return Optional.empty();
    }

    double averageMilliseconds =
        completedTasks.stream()
            .mapToLong(TaskOnlyResource::timePassed)
            .average()
            .orElse(0);

    double averageDays =
        averageMilliseconds /
            (1000d * 60 * 60 * 24);

    return Optional.of(
        new AvgCompletionTimeDTO(
            "AVG_COMPLETION_TIME_MEMBER",
            averageDays,
            Map.of("completedTasks", completedTasks.size())
        )
    );
  }

  public Optional<RescheduledTasksDTO> handle(
      GetRescheduledTasksForMemberQuery query
  ) {

    var tasks =
        tasksQueryPort.getTasksByMemberId(
            query.memberId()
        );

    long totalRescheduledTimes =
        tasks.stream()
            .mapToLong(TaskOnlyResource::timesRearranged)
            .sum();

    return Optional.of(
        new RescheduledTasksDTO(
            "RESCHEDULED_TASKS_MEMBER",
            totalRescheduledTimes,
            Map.of(
                "total", tasks.size(),
                "rescheduled", (int) totalRescheduledTimes
            ),
            totalRescheduledTimes > 0
                ? List.of(query.memberId())
                : List.of()
        )
    );
  }

  public Optional<TaskDistributionDTO> handle(
      GetTaskDistributionForMemberQuery query
  ) {

    var tasks =
        tasksQueryPort.getTasksByMemberId(
            query.memberId()
        );

    var member =
        iamQueryPort.getUserOnlyByMemberId(
            query.memberId()
        );

    String memberName;

    if (member == null) {

      memberName = "Unknown Member";

    } else {

      memberName =
          String.format(
              "%s %s",
              member.name(),
              member.surname()
          ).trim();
    }

    var details =
        Map.of(
            query.memberId().toString(),
            new MemberTaskInfoDTO(
                memberName,
                tasks.size()
            )
        );

    return Optional.of(
        new TaskDistributionDTO(
            "TASK_DISTRIBUTION_MEMBER",
            tasks.size(),
            details
        )
    );
  }

  public Optional<TaskOverviewDTO> handle(
      GetTaskOverviewForMemberQuery query
  ) {

    var tasks =
        tasksQueryPort.getTasksByMemberId(
            query.memberId()
        );

    var overview =
        tasks.stream()
            .collect(
                Collectors.groupingBy(
                    TaskOnlyResource::status,
                    Collectors.counting()
                )
            );

    var details =
        overview.entrySet()
            .stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().intValue()
                )
            );

    return Optional.of(
        new TaskOverviewDTO(
            "TASK_OVERVIEW_MEMBER",
            tasks.size(),
            details
        )
    );
  }
}