package com.collabrium.tasks.management.interfaces.rest.controllers;

import com.collabrium.tasks.management.domain.model.queries.GetMemberByIdQuery;
import com.collabrium.tasks.management.domain.services.MemberQueryService;
import com.collabrium.tasks.management.interfaces.rest.resources.MemberOnlyResource;
import com.collabrium.tasks.management.interfaces.rest.transform.MemberOnlyResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@Tag(name = "Member", description = "Member management API")
public class MemberController {

  private final MemberQueryService memberQueryService;

  public MemberController(
      MemberQueryService memberQueryService
  ) {

    this.memberQueryService = memberQueryService;
  }

  @GetMapping("{memberId}")
  @Operation(summary = "Get member details by id", description = "Fetches the details of the member.")
  public ResponseEntity<MemberOnlyResource> getMemberById(
      @PathVariable Long memberId
  ) {

    var getMemberByIdQuery = new GetMemberByIdQuery(memberId);
    var memberOptional = memberQueryService.handle(getMemberByIdQuery);
    if (memberOptional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var memberResource = MemberOnlyResourceFromEntityAssembler.toResourceFromEntity(memberOptional.get());
    return ResponseEntity.ok(memberResource);
  }
}