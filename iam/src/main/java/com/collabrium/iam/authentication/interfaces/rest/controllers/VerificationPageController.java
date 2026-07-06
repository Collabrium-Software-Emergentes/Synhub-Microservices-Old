package com.collabrium.iam.authentication.interfaces.rest.controllers;

import com.collabrium.iam.authentication.domain.exceptions.InvalidTokenException;
import com.collabrium.iam.authentication.domain.exceptions.UserNotFoundException;
import com.collabrium.iam.authentication.domain.model.commands.VerifyUserCommand;
import com.collabrium.iam.authentication.domain.services.UserCommandService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerificationPageController {

  private final UserCommandService userCommandService;

  public VerificationPageController(UserCommandService userCommandService) {
    this.userCommandService = userCommandService;
  }

  @GetMapping("/verification/complete")
  public String completeVerification(
      @RequestParam String token,
      Model model
  ) {
    var verifiedUser = userCommandService.handle(new VerifyUserCommand(token))
        .orElseThrow(() -> new UserNotFoundException("unknown"));

    model.addAttribute("username", verifiedUser.getUsername());
    model.addAttribute("email", verifiedUser.getEmail());
    return "verification-success";
  }

  @ExceptionHandler({InvalidTokenException.class, UserNotFoundException.class})
  public String handleVerificationError(Exception exception, Model model) {
    model.addAttribute("message", exception.getMessage());
    return "verification-error";
  }
}
