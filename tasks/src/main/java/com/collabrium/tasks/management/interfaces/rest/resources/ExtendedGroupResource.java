package com.collabrium.tasks.management.interfaces.rest.resources;

import java.util.List;

public record ExtendedGroupResource(
    Long id,
    String name,
    String imgUrl,
    String description,
    String code,
    List<MemberResource> members
) {
}