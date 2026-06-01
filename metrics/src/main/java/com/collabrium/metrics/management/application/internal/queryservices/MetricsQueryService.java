package com.collabrium.metrics.management.application.internal.queryservices;

import com.collabrium.metrics.management.application.internal.dto.AvgCompletionTimeDTO;
import com.collabrium.metrics.management.application.internal.dto.RescheduledTasksDTO;
import com.collabrium.metrics.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.metrics.management.domain.model.queries.GetAvgCompletionTimeForMemberQuery;
import com.collabrium.metrics.management.domain.model.queries.GetRescheduledTasksForMemberQuery;
import com.collabrium.metrics.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MetricsQueryService {

  private static final String TASK_STATUS = "DONE";

  private final TasksQueryPort tasksQueryPort;

  public MetricsQueryService(
      TasksQueryPort tasksQueryPort
  ) {

    this.tasksQueryPort = tasksQueryPort;
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
}