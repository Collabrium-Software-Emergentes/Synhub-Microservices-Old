package com.collabrium.requests.management.application.internal.assemblers;

import com.collabrium.requests.management.application.internal.dto.RequestDetailsDTO;
import com.collabrium.requests.management.application.internal.dto.TaskDetailsDTO;
import com.collabrium.requests.management.domain.model.aggregates.Request;

public class RequestDetailsDTOFromEntityAssembler {

  private RequestDetailsDTOFromEntityAssembler() {
  }

  public static RequestDetailsDTO toDTO(
    Request request,
    TaskDetailsDTO taskDetails
  ) {

    return new RequestDetailsDTO(
      request.getId(),
      request.getDescription(),
      request.getRequestType(),
      request.getRequestStatus(),
      request.getImageUrl(),
      taskDetails
    );
  }
}