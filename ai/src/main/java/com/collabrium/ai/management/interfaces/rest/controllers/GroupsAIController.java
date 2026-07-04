package com.collabrium.ai.management.interfaces.rest.controllers;

import com.collabrium.ai.management.domain.services.GroupAICommandService;
import com.collabrium.ai.management.interfaces.rest.resources.GenerateGroupSuggestionResource;
import com.collabrium.ai.management.interfaces.rest.resources.GroupSuggestionResource;
import com.collabrium.ai.management.interfaces.rest.transform.GenerateGroupSuggestionCommandFromResourceAssembler;
import com.collabrium.ai.management.interfaces.rest.transform.GroupSuggestionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "/api/v1/ai/groups",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(
    name = "AI Groups",
    description = "AI Group Generation Endpoints"
)
public class GroupsAIController {

  private final GroupAICommandService groupAICommandService;

  public GroupsAIController(
      GroupAICommandService groupAICommandService
  ) {
    this.groupAICommandService = groupAICommandService;
  }

  @PostMapping("/suggestion")
  @Operation(
      summary = "Generate a group suggestion",
      description = "Generates a group name and description using AI"
  )
  public ResponseEntity<GroupSuggestionResource> generateSuggestion(
      @Valid @RequestBody
      GenerateGroupSuggestionResource resource
  ) {

    var command =
        GenerateGroupSuggestionCommandFromResourceAssembler
            .toCommandFromResource(resource);

    var suggestion =
        groupAICommandService.handle(command);

    var response =
        GroupSuggestionResourceFromEntityAssembler
            .toResourceFromEntity(suggestion);

    return ResponseEntity.ok(response);
  }
}