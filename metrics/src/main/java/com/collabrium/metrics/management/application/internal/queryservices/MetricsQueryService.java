package com.collabrium.metrics.management.application.internal.queryservices;

import com.collabrium.metrics.management.application.internal.assemblers.TaskDistributionDetailsAssembler;
import com.collabrium.metrics.management.application.internal.assemblers.TaskOverviewDetailsAssembler;
import com.collabrium.metrics.management.application.internal.dto.*;
import com.collabrium.metrics.management.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.metrics.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.metrics.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.metrics.management.domain.exceptions.GroupNotFoundException;
import com.collabrium.metrics.management.domain.exceptions.UserIsNotLeaderException;
import com.collabrium.metrics.management.domain.exceptions.UserNotFoundException;
import com.collabrium.metrics.management.domain.model.queries.*;
import com.collabrium.metrics.shared.infrastructure.clients.tasks.resources.TaskOnlyResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class MetricsQueryService {

  private static final Set<String> COMPLETED_STATUSES =
    Set.of("COMPLETED", "DONE");

  private static final double MILLISECONDS_PER_SECOND = 1000d;
  private static final double SECONDS_PER_MINUTE = 60d;
  private static final double MINUTES_PER_HOUR = 60d;
  private static final double HOURS_PER_DAY = 24d;
  private static final double MILLISECONDS_PER_DAY =
    MILLISECONDS_PER_SECOND *
      SECONDS_PER_MINUTE *
      MINUTES_PER_HOUR *
      HOURS_PER_DAY;

  private static final String AVG_COMPLETION_TIME_MEMBER =
    "AVG_COMPLETION_TIME_MEMBER";

  private static final String RESCHEDULED_TASKS_MEMBER =
    "RESCHEDULED_TASKS_MEMBER";

  private static final String TASK_DISTRIBUTION_MEMBER =
    "TASK_DISTRIBUTION_MEMBER";

  private static final String TASK_OVERVIEW_MEMBER =
    "TASK_OVERVIEW_MEMBER";

  private static final String TASK_OVERVIEW =
    "TASK_OVERVIEW";

  private static final String UNKNOWN_MEMBER =
    "Unknown Member";

  private static final String TASK_DISTRIBUTION =
    "TASK_DISTRIBUTION";

  private final TasksQueryPort tasksQueryPort;
  private final IamQueryPort iamQueryPort;
  private final GroupsQueryPort groupsQueryPort;

  public MetricsQueryService(
    TasksQueryPort tasksQueryPort,
    IamQueryPort iamQueryPort,
    GroupsQueryPort groupsQueryPort
  ) {

    this.tasksQueryPort = tasksQueryPort;
    this.iamQueryPort = iamQueryPort;
    this.groupsQueryPort = groupsQueryPort;
  }

  public Optional<AvgCompletionTimeDTO> handle(
      GetAvgCompletionTimeForMemberQuery query
  ) {

    var tasks = getMemberTasks(query.memberId());

    var completedTasks =
      getCompletedTasks(
        tasks
      );

    if (completedTasks.isEmpty()) {
      return Optional.empty();
    }

    double averageMilliseconds =
        calculateAverageTimePassed(
            completedTasks
        );

    double averageDays =
      averageMilliseconds /
        MILLISECONDS_PER_DAY;

    return Optional.of(
      new AvgCompletionTimeDTO(
        AVG_COMPLETION_TIME_MEMBER,
        averageDays,
        Map.of(
          "completedTasks",
          completedTasks.size()
        )
      )
    );
  }

  public Optional<RescheduledTasksDTO> handle(
      GetRescheduledTasksForMemberQuery query
  ) {

    var tasks = getMemberTasks(query.memberId());

    long totalRescheduledTimes =
        tasks.stream()
            .mapToLong(TaskOnlyResource::timesRearranged)
            .sum();

    return Optional.of(
        new RescheduledTasksDTO(
            RESCHEDULED_TASKS_MEMBER,
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

    var tasks = getMemberTasks(query.memberId());

    var details =
      Map.of(
        query.memberId().toString(),
        new MemberTaskInfoDTO(
          getMemberName(query.memberId()),
          tasks.size()
        )
      );

    return Optional.of(
        new TaskDistributionDTO(
            TASK_DISTRIBUTION_MEMBER,
            tasks.size(),
            details
        )
    );
  }

  public Optional<TaskOverviewDTO> handle(
      GetTaskOverviewForMemberQuery query
  ) {

    var tasks = getMemberTasks(query.memberId());

    var details =
      TaskOverviewDetailsAssembler.fromTasks(
        tasks
      );

    return Optional.of(
        new TaskOverviewDTO(
            TASK_OVERVIEW_MEMBER,
            tasks.size(),
            details
        )
    );
  }

  public Optional<TaskTimePassedDTO> handle(
    GetTaskTimePassedQuery query
  ) {

    var tasks =
      getMemberTasks(
        query.memberId()
      );

    var completedTasks =
      getCompletedTasks(
        tasks
      );

    Long averageTimePassed =
        Math.round(
            calculateAverageTimePassed(
                completedTasks
            )
        );

    return Optional.of(
      new TaskTimePassedDTO(
        query.memberId(),
        averageTimePassed
      )
    );
  }

  public Optional<TaskOverviewDTO> handle(
    GetTasksOverviewOfMyGroupQuery query
  ) {

    var tasks =
        getGroupTasksByLeaderUser(
            query.userId()
        );

    var details =
      TaskOverviewDetailsAssembler.fromTasks(
        tasks
      );

    return Optional.of(
      new TaskOverviewDTO(
        TASK_OVERVIEW,
        tasks.size(),
        details
      )
    );
  }

  public Optional<TaskDistributionDTO> handle(
    GetTaskDistributionOfMyGroupQuery query
  ){

    var tasks =
        getGroupTasksByLeaderUser(
            query.userId()
        );

    var details =
        TaskDistributionDetailsAssembler.fromTasks(
            tasks,
            this::getMemberName
        );

    return Optional.of(
      new TaskDistributionDTO(
        TASK_DISTRIBUTION,
        tasks.size(),
        details
      )
    );
  }

  private List<TaskOnlyResource> getMemberTasks(
    Long memberId
  ) {

    return tasksQueryPort.getTasksByMemberId(
      memberId
    );
  }

  private String getMemberName(
    Long memberId
  ) {

    var member =
      iamQueryPort.getUserOnlyByMemberId(
        memberId
      );

    if (member == null) {
      return UNKNOWN_MEMBER;
    }

    return String.format(
      "%s %s",
      member.name(),
      member.surname()
    ).trim();
  }

  private List<TaskOnlyResource> getCompletedTasks(
    List<TaskOnlyResource> tasks
  ) {

    return tasks.stream()
      .filter(task ->
        COMPLETED_STATUSES.contains(
          task.status()
        )
      )
      .toList();
  }

  private List<TaskOnlyResource> getGroupTasksByLeaderUser(
      Long userId
  ) {

    var user =
        iamQueryPort.getUserOnlyById(
            userId
        );

    if (user == null) {
      throw UserNotFoundException.forId(
          userId
      );
    }

    if (user.leaderId() == null) {
      throw UserIsNotLeaderException.forUser(
          userId
      );
    }

    var group =
        groupsQueryPort.getGroupByLeaderId(
            user.leaderId()
        );

    if (group == null) {
      throw GroupNotFoundException.forLeader(
          user.leaderId()
      );
    }

    return tasksQueryPort.getSimpleTasksByGroupId(
        group.id()
    );
  }

  private double calculateAverageTimePassed(
      List<TaskOnlyResource> tasks
  ) {

    return tasks.stream()
        .mapToLong(TaskOnlyResource::timePassed)
        .average()
        .orElse(0);
  }
}