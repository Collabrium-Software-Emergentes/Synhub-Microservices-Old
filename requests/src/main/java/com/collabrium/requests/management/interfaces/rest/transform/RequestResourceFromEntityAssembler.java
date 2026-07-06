package com.collabrium.requests.management.interfaces.rest.transform;

import com.collabrium.requests.management.domain.model.aggregates.Request;
import com.collabrium.requests.management.interfaces.rest.resources.RequestResource;

public class RequestResourceFromEntityAssembler {

  private RequestResourceFromEntityAssembler() {
  }

  public static RequestResource toResourceFromEntity(
      Request entity
  ) {

    return new RequestResource(
        entity.getId(),
        entity.getDescription(),
        entity.getRequestType(),
        entity.getRequestStatus(),
        entity.getImageUrl(),
        entity.getTaskId().value()
    );
  }
}