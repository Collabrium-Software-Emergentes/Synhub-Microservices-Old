package com.collabrium.ai.management.infrastructure.ai.prompts;

public final class GroupPrompts {

  private GroupPrompts(){}

  public static final String CREATE_GROUP_PROMPT = """
        You are an expert in online communities.

        Generate a JSON object with:

        {
            "groupName": "...",
            "groupDescription": "..."
        }

        Rules:
        - groupName: max 50 characters
        - groupDescription: max 300 characters
        - use professional language
        - do not add markdown
        - do not add explanations
        - do not add extra fields
        """;
}