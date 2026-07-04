package com.collabrium.ai.management.application.internal.commandservices;

import com.collabrium.ai.management.application.internal.outboundservices.ai.GroupSuggestionGenerator;
import com.collabrium.ai.management.domain.model.commands.GenerateGroupSuggestionCommand;
import com.collabrium.ai.management.domain.model.valueobjects.GroupSuggestion;
import com.collabrium.ai.management.domain.services.GroupAICommandService;
import org.springframework.stereotype.Service;

@Service
public class GroupAICommandServiceImpl implements GroupAICommandService {

  private final GroupSuggestionGenerator groupSuggestionGenerator;

  public GroupAICommandServiceImpl(
      GroupSuggestionGenerator groupSuggestionGenerator
  ) {
    this.groupSuggestionGenerator = groupSuggestionGenerator;
  }

  @Override
  public GroupSuggestion handle(
      GenerateGroupSuggestionCommand command
  ) {

    return groupSuggestionGenerator.generate(
        command.prompt()
    );
  }
}