package com.collabrium.metrics.management.interfaces.rest.transform;

import com.collabrium.metrics.management.application.internal.dto.TaskDistributionDTO;
import com.collabrium.metrics.management.interfaces.rest.resources.TaskDistributionResource;

import java.util.Map;
import java.util.stream.Collectors;

public class TaskDistributionResourceFromDTOAssembler {

  private TaskDistributionResourceFromDTOAssembler() {
  }

  public static TaskDistributionResource toResourceFromDTO(
      TaskDistributionDTO dto
  ) {

    var details =
        dto.details()
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> MemberTaskInfoResourceFromDTOAssembler
                    .toResourceFromDTO(entry.getValue())
            ));

    return new TaskDistributionResource(
        dto.type(),
        dto.value(),
        details
    );
  }
}