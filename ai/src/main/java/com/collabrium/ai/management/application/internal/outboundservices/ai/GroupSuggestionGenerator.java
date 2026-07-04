package com.collabrium.ai.management.application.internal.outboundservices.ai;

import com.collabrium.ai.management.domain.model.valueobjects.GroupSuggestion;

public interface GroupSuggestionGenerator {

  GroupSuggestion generate(String prompt);
}