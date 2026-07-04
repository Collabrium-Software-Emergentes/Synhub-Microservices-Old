package com.collabrium.ai.management.interfaces.rest.transform;

import com.collabrium.ai.management.domain.model.valueobjects.GroupSuggestion;
import com.collabrium.ai.management.interfaces.rest.resources.GroupSuggestionResource;

public class GroupSuggestionResourceFromEntityAssembler {

  public static GroupSuggestionResource toResourceFromEntity(
      GroupSuggestion suggestion
  ) {

    return new GroupSuggestionResource(
        suggestion.groupName(),
        suggestion.groupDescription()
    );
  }
}