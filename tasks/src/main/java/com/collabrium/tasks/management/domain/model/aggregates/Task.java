package com.collabrium.tasks.management.domain.model.aggregates;

import com.collabrium.tasks.management.domain.exceptions.InvalidTaskException;
import com.collabrium.tasks.management.domain.model.commands.CreateTaskCommand;
import com.collabrium.tasks.management.domain.model.commands.UpdateTaskCommand;
import com.collabrium.tasks.management.domain.model.commands.UpdateTaskStatusCommand;
import com.collabrium.tasks.management.domain.model.valueobjects.GroupId;
import com.collabrium.tasks.management.domain.model.valueobjects.TaskStatus;
import com.collabrium.tasks.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AttributeOverrides({
    @AttributeOverride(
        name = "groupId.value",
        column = @Column(name = "group_id")
    )
})
public class Task extends AuditableAbstractAggregateRoot<Task> {

  @NonNull
  @Column(nullable = false)
  private String title;

  @NonNull
  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TaskStatus status;

  @NonNull
  @Column(name = "due_date", nullable = false)
  private OffsetDateTime dueDate;

  @Setter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Setter
  @Embedded
  private GroupId groupId;

  @Column(nullable = false)
  private Integer timesRearranged = 0;

  @Column(nullable = false)
  private Long timePassed = 0L;

  @Column(name = "public_id")
  private String publicId;

  @Column(name = "image_url", columnDefinition = "TEXT")
  private String imageUrl;

  public Task(CreateTaskCommand command) {

    validateCreation(command);

    this.title = command.title();
    this.description = command.description();
    this.dueDate = command.dueDate();
    this.status = resolveInitialStatus(command.dueDate());
    this.timesRearranged = 0;
    this.timePassed = 0L;
  }

  private void validateCreation(CreateTaskCommand command) {

    if (command == null) {
      throw InvalidTaskException.forNullCreateCommand();
    }

    if (command.title() == null) {
      throw InvalidTaskException.forNullTitle();
    }

    if (command.title().isBlank()) {
      throw InvalidTaskException.forEmptyTitle();
    }

    if (command.description() == null) {
      throw InvalidTaskException.forNullDescription();
    }

    if (command.description().isBlank()) {
      throw InvalidTaskException.forEmptyDescription();
    }

    if (command.dueDate() == null) {
      throw InvalidTaskException.forNullDueDate();
    }
  }

  public void updateStatus(UpdateTaskStatusCommand command) {

    validateStatusUpdate(command);

    TaskStatus newStatus =
        TaskStatus.valueOf(command.status());

    updateRearrangementMetrics(newStatus);

    this.status = newStatus;
  }

  public void updateImage(String imageUrl, String publicId) {
    this.imageUrl = imageUrl;
    this.publicId = publicId;
  }


  private void validateStatusUpdate(UpdateTaskStatusCommand command) {

    if (command == null) {
      throw InvalidTaskException.forNullUpdateStatusCommand();
    }

    if (command.status() == null) {
      throw InvalidTaskException.forNullStatus();
    }

    try {
      TaskStatus.valueOf(command.status());
    } catch (IllegalArgumentException ex) {
      throw InvalidTaskException.forInvalidStatus(command.status());
    }
  }

  private void updateRearrangementMetrics(TaskStatus newStatus) {

    if (this.status == TaskStatus.IN_PROGRESS &&
        isFinishedStatus(newStatus)) {

      OffsetDateTime now =
          OffsetDateTime.now(ZoneOffset.UTC);

      if (timesRearranged > 0) {

        long updatedAt =
            this.getUpdatedAt()
                .toInstant()
                .toEpochMilli();

        this.timePassed +=
            now.toInstant().toEpochMilli() - updatedAt;

      } else {

        this.timePassed =
            now.toInstant().toEpochMilli()
                - this.getCreatedAt()
                .toInstant()
                .toEpochMilli();
      }
    }

    else if (
        (isFinishedStatus(this.status)
            || this.status == TaskStatus.ON_HOLD
            || this.status == TaskStatus.EXPIRED)
            &&
            newStatus == TaskStatus.IN_PROGRESS
    ) {

      this.timesRearranged++;
    }
  }

  private boolean isFinishedStatus(TaskStatus status) {
    return status == TaskStatus.COMPLETED
        || status == TaskStatus.DONE;
  }

  public void updateTask(UpdateTaskCommand command) {

    validateUpdate(command);

    if (command.title() != null &&
        !command.title().isBlank()) {

      this.title = command.title();
    }

    if (command.description() != null &&
        !command.description().isBlank()) {

      this.description = command.description();
    }

    if (command.dueDate() != null) {

      this.dueDate = command.dueDate();

      this.status =
          resolveInitialStatus(command.dueDate());
    }
  }

  private void validateUpdate(UpdateTaskCommand command) {

    if (command == null) {
      throw InvalidTaskException.forNullUpdateCommand();
    }
  }

  private TaskStatus resolveInitialStatus(
      OffsetDateTime dueDate
  ) {

    if (dueDate.isBefore(
        OffsetDateTime.now(ZoneOffset.UTC))) {

      return TaskStatus.EXPIRED;
    }

    return TaskStatus.IN_PROGRESS;
  }

  @Override
  public String toString() {

    return String.format(
        "Task{id=%d, title=%s, status=%s, dueDate=%s, rearranged=%d}",
        getId() != null ? getId() : 0,
        title,
        status,
        dueDate,
        timesRearranged
    );
  }
}
