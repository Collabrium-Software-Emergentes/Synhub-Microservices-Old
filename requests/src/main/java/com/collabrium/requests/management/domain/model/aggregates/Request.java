package com.collabrium.requests.management.domain.model.aggregates;

import com.collabrium.requests.management.domain.exceptions.InvalidRequestException;
import com.collabrium.requests.management.domain.model.commands.CreateRequestCommand;
import com.collabrium.requests.management.domain.model.valueobjects.RequestStatus;
import com.collabrium.requests.management.domain.model.valueobjects.RequestType;
import com.collabrium.requests.management.domain.model.valueobjects.TaskId;
import com.collabrium.requests.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Entity
@Table(name = "requests")
@AttributeOverrides({
    @AttributeOverride(
        name = "taskId.value",
        column = @Column(name = "task_id")
    )
})
public class Request
    extends AuditableAbstractAggregateRoot<Request> {

  @NonNull
  @Column(nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "request_type", nullable = false)
  private RequestType requestType;

  @Enumerated(EnumType.STRING)
  @Column(name = "request_status", nullable = false)
  private RequestStatus requestStatus;

  @Embedded
  @Setter
  private TaskId taskId;

  protected Request() {
  }

  public Request(CreateRequestCommand command) {

    validateDescription(command.description());

    this.description = command.description().trim();
    this.requestType =
        RequestType.fromString(command.requestType());

    this.requestStatus = RequestStatus.PENDING;

    this.taskId =
        new TaskId(command.taskId());
  }

  public String getRequestType() {
    return requestType.name();
  }

  public String getRequestStatus() {
    return requestStatus.name();
  }

  public void updateRequestStatus(
      String requestStatus
  ) {

    var newStatus =
        RequestStatus.fromString(requestStatus);

    validateStatusTransition(newStatus);

    this.requestStatus = newStatus;
  }

  private void validateDescription(
      String description
  ) {

    if (description == null) {
      throw InvalidRequestException
          .forNullDescription();
    }

    if (description.isBlank()) {
      throw InvalidRequestException
          .forEmptyDescription();
    }
  }

  private void validateStatusTransition(
      RequestStatus newStatus
  ) {

    if (
        this.requestStatus == RequestStatus.REJECTED &&
            newStatus == RequestStatus.PENDING
    ) {

      throw InvalidRequestException
          .forInvalidStatusTransition(
              this.requestStatus.name(),
              newStatus.name()
          );
    }
  }
}