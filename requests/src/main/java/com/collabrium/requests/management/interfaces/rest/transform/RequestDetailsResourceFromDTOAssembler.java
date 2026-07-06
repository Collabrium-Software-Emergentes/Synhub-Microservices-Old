package com.collabrium.requests.management.interfaces.rest.transform;

import com.collabrium.requests.management.application.internal.dto.RequestDetailsDTO;
import com.collabrium.requests.management.interfaces.rest.resources.RequestDetailsResource;

public class RequestDetailsResourceFromDTOAssembler {

  private RequestDetailsResourceFromDTOAssembler() {
  }

  public static RequestDetailsResource toResourceFromDTO(
      RequestDetailsDTO dto
  ) {

    return new RequestDetailsResource(
        dto.id(),
        dto.description(),
        dto.requestType(),
        dto.requestStatus(),
        dto.imageUrl(),
        TaskResourceFromDTOAssembler.toResourceFromDTO(dto.task())
    );
  }
}