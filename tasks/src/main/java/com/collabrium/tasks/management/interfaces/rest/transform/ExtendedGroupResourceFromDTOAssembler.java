package com.collabrium.tasks.management.interfaces.rest.transform;

import com.collabrium.tasks.management.application.internal.dto.ExtendedGroupDTO;
import com.collabrium.tasks.management.interfaces.rest.resources.ExtendedGroupResource;

public class ExtendedGroupResourceFromDTOAssembler {

  private ExtendedGroupResourceFromDTOAssembler() {
  }

  public static ExtendedGroupResource toResourceFromDTO(
      ExtendedGroupDTO dto
  ) {

    var members =
        dto.members()
            .stream()
            .map(
                MemberResourceFromDTOAssembler
                    ::toResourceFromDTO
            )
            .toList();

    return new ExtendedGroupResource(
        dto.id(),
        dto.name(),
        dto.imgUrl(),
        dto.description(),
        dto.code(),
        members
    );
  }
}