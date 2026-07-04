package com.collabrium.ai.management.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenerateGroupSuggestionResource(

    @NotBlank
    @Size(min = 10, max = 500)
    String prompt
) {
}