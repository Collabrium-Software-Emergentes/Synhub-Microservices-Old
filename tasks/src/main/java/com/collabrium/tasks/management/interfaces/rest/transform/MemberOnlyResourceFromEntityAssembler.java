package com.collabrium.tasks.management.interfaces.rest.transform;

import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.interfaces.rest.resources.MemberOnlyResource;

public class MemberOnlyResourceFromEntityAssembler {

  private MemberOnlyResourceFromEntityAssembler() {
  }

  public static MemberOnlyResource toResourceFromEntity(
      Member entity
  ) {

    Long groupId = entity.getGroupId() != null
        ? entity.getGroupId().value()
        : null;

    return new MemberOnlyResource(
        entity.getId(),
        groupId
    );
  }
}