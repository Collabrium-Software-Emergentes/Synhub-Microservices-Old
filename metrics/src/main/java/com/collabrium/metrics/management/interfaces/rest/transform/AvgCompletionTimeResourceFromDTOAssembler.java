package com.collabrium.metrics.management.interfaces.rest.transform;

import com.collabrium.metrics.management.application.internal.dto.AvgCompletionTimeDTO;
import com.collabrium.metrics.management.interfaces.rest.resources.AvgCompletionTimeResource;

public class AvgCompletionTimeResourceFromDTOAssembler {

  private AvgCompletionTimeResourceFromDTOAssembler() {
  }

  public static AvgCompletionTimeResource toResourceFromDTO(
      AvgCompletionTimeDTO dto
  ) {

    return new AvgCompletionTimeResource(
        dto.type(),
        dto.value(), 
        dto.details()
    );
  }
}