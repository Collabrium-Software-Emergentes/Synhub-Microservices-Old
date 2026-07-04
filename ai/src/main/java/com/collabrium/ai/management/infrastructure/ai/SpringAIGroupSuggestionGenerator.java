package com.collabrium.ai.management.infrastructure.ai;

import com.collabrium.ai.management.application.internal.outboundservices.ai.GroupSuggestionGenerator;
import com.collabrium.ai.management.domain.model.valueobjects.GroupSuggestion;
import com.collabrium.ai.management.infrastructure.ai.prompts.GroupPrompts;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class SpringAIGroupSuggestionGenerator implements GroupSuggestionGenerator {

  private final ChatClient chatClient;

  public SpringAIGroupSuggestionGenerator(
      ChatClient.Builder builder
  ) {

    this.chatClient = builder.build();
  }

  @Override
  public GroupSuggestion generate(String prompt) {

    return chatClient
        .prompt()
        .system(GroupPrompts.CREATE_GROUP_PROMPT)
        .user(prompt)
        .call()
        .entity(GroupSuggestion.class);
  }
}