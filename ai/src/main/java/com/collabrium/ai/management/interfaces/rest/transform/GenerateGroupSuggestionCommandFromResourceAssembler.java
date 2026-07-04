package com.collabrium.ai.management.interfaces.rest.transform;

import com.collabrium.ai.management.domain.model.commands.GenerateGroupSuggestionCommand;
import com.collabrium.ai.management.interfaces.rest.resources.GenerateGroupSuggestionResource;

public class GenerateGroupSuggestionCommandFromResourceAssembler {

  private GenerateGroupSuggestionCommandFromResourceAssembler() {
  }

  public static GenerateGroupSuggestionCommand toCommandFromResource(
      GenerateGroupSuggestionResource resource
  ) {

    return new GenerateGroupSuggestionCommand(
        resource.prompt()
    );
  }
}