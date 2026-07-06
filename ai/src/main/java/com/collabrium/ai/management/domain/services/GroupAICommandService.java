package com.collabrium.ai.management.domain.services;

import com.collabrium.ai.management.domain.model.commands.GenerateGroupSuggestionCommand;
import com.collabrium.ai.management.domain.model.valueobjects.GroupSuggestion;

public interface GroupAICommandService {

  GroupSuggestion handle(GenerateGroupSuggestionCommand command);
}