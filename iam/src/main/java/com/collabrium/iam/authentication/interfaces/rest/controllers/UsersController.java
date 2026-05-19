package com.collabrium.iam.authentication.interfaces.rest.controllers;

import com.collabrium.iam.authentication.domain.model.queries.*;
import com.collabrium.iam.authentication.domain.services.UserQueryService;
import com.collabrium.iam.authentication.interfaces.rest.resources.UserResource;
import com.collabrium.iam.authentication.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is a REST controller that exposes the user's resource.
 * It includes the following operations:
 * - GET /api/v1/users: returns all the users
 * - GET /api/v1/users/{userId}: returns the user with the given id
 **/
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User Management Endpoints")
public class UsersController {

  private final UserQueryService userQueryService;

  public UsersController(UserQueryService userQueryService) {
    this.userQueryService = userQueryService;
  }


}
