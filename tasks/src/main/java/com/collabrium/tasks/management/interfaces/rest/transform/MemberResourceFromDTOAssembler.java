package com.collabrium.tasks.management.interfaces.rest.transform;

import com.collabrium.tasks.management.application.internal.dto.MemberDetailsDTO;
import com.collabrium.tasks.management.interfaces.rest.resources.MemberResource;

public class MemberResourceFromDTOAssembler {

  private MemberResourceFromDTOAssembler() {
  }

  public static MemberResource toResourceFromDTO(MemberDetailsDTO dto) {

    return new MemberResource(
        dto.memberId(),
        dto.username(),
        dto.name(),
        dto.surname(),
        dto.imgUrl(),
        dto.email(),
        dto.groupId()
    );
  }
}