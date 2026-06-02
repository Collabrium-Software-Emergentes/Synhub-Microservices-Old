package com.collabrium.metrics.management.interfaces.rest.transform;

import com.collabrium.metrics.management.application.internal.dto.MemberTaskInfoDTO;
import com.collabrium.metrics.management.interfaces.rest.resources.MemberTaskInfoResource;

public class MemberTaskInfoResourceFromDTOAssembler {

  private MemberTaskInfoResourceFromDTOAssembler() {
  }

  public static MemberTaskInfoResource toResourceFromDTO(
      MemberTaskInfoDTO dto
  ) {

    return new MemberTaskInfoResource(
        dto.memberName(),
        dto.taskCount()
    );
  }
}