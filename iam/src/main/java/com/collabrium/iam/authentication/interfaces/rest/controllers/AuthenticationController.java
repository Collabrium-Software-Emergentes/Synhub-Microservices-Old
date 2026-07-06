package com.collabrium.iam.authentication.interfaces.rest.controllers;

import com.collabrium.iam.authentication.domain.services.UserCommandService;
import com.collabrium.iam.authentication.interfaces.rest.resources.AuthenticatedUserResource;
import com.collabrium.iam.authentication.interfaces.rest.resources.SignInResource;
import com.collabrium.iam.authentication.interfaces.rest.resources.SignUpResource;
import com.collabrium.iam.authentication.interfaces.rest.resources.UserResource;
import com.collabrium.iam.authentication.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.collabrium.iam.authentication.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.collabrium.iam.authentication.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.collabrium.iam.authentication.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.collabrium.iam.authentication.domain.model.commands.VerifyUserCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthenticationController
 * <p>
 *     This controller is responsible for handling authentication requests.
 *     It exposes two endpoints:
 *     <ul>
 *         <li>POST /api/v1/auth/sign-in</li>
 *         <li>POST /api/v1/auth/sign-up</li>
 *         <li>GET /api/v1/authentication/verify?token=...</li>
 *     </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthenticationController {

  private final UserCommandService userCommandService;

  public AuthenticationController(UserCommandService userCommandService) {
    this.userCommandService = userCommandService;
  }

  /**
   * Handles the sign-in request.
   * @param signInResource the sign-in request body.
   * @return the authenticated user resource.
   */
  @PostMapping("/sign-in")
  public ResponseEntity<AuthenticatedUserResource> signIn(
      @RequestBody SignInResource signInResource
  ) {

    var signInCommand = SignInCommandFromResourceAssembler
        .toCommandFromResource(signInResource);

    var authenticatedUser = userCommandService.handle(signInCommand);

    if (authenticatedUser.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler
        .toResourceFromEntity(
            authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());

    return ResponseEntity.ok(authenticatedUserResource);
  }

  /**
   * Handles the sign-up request.
   * @param signUpResource the sign-up request body.
   * @return the created user resource.
   */
  @PostMapping("/sign-up")
  public ResponseEntity<UserResource> signUp(
      @RequestBody SignUpResource signUpResource,
      HttpServletRequest request
  ) {

    var signUpCommand = SignUpCommandFromResourceAssembler
        .toCommandFromResource(signUpResource, buildBaseUrl(request));

    var user = userCommandService.handle(signUpCommand);

    if (user.isEmpty()) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    var userResource = UserResourceFromEntityAssembler
        .toResourceFromEntity(user.get());

    return new ResponseEntity<>(userResource, HttpStatus.CREATED);
  }

  @GetMapping("/verify")
  public ResponseEntity<UserResource> verify(
      @RequestParam String token
  ) {
    var verifiedUser = userCommandService.handle(new VerifyUserCommand(token));

    if (verifiedUser.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    return ResponseEntity.ok(
        UserResourceFromEntityAssembler.toResourceFromEntity(verifiedUser.get()));
  }

  private String buildBaseUrl(HttpServletRequest request) {
    var baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    if (request.getContextPath() != null && !request.getContextPath().isBlank()) {
      baseUrl = baseUrl + request.getContextPath();
    }
    return baseUrl;
  }
}