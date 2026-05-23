package com.collabrium.tasks.management.application.internal.dto;

import java.util.List;

public record ExtendedGroupDTO(
    Long id,
    String name,
    String imgUrl,
    String description,
    String code,
    List<MemberDetailsDTO> members
) {
}