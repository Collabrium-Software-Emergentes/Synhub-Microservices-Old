package com.collabrium.notifications.management.infrastructure.email;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailTemplateFactory {

  private final TemplateEngine templateEngine;

  public EmailTemplateFactory(
      TemplateEngine templateEngine
  ) {

    this.templateEngine = templateEngine;
  }

  public String buildGroupCreatedEmailTemplate(
      String groupName,
      String groupDescription,
      String groupImageUrl,
      String code
  ) {

    String groupUrl = "synhub://group/" + code;

    Context context = new Context();

    context.setVariable("groupName", groupName);
    context.setVariable("groupDescription", groupDescription);
    context.setVariable("groupImage", groupImageUrl);
    context.setVariable("groupCode", code);
    context.setVariable("groupUrl", groupUrl);

    return templateEngine.process(
        "groups/group-created-email",
        context
    );
  }

  public String buildInvitationAcceptedEmailTemplate(
      String groupName,
      String groupDescription,
      String groupImageUrl,
      String code
  ) {

    Context context = new Context();

    context.setVariable("groupName", groupName);
    context.setVariable("groupDescription", groupDescription);
    context.setVariable("groupImage", groupImageUrl);
    context.setVariable("groupCode", code);

    return templateEngine.process(
        "groups/member-accepted-email",
        context
    );
  }

  public String buildInvitationCreatedEmailTemplate(
      String memberUsername,
      String memberName,
      String memberSurname,
      String memberImgUrl,
      String memberEmail
  ) {

    Context context = new Context();

    context.setVariable("memberUsername", memberUsername);
    context.setVariable("memberName", memberName);
    context.setVariable("memberSurname", memberSurname);
    context.setVariable("memberImage", memberImgUrl);
    context.setVariable("memberEmail", memberEmail);

    return templateEngine.process(
        "groups/invitation-created-email",
        context
    );
  }

  public String buildRemoveMemberFromGroupEmailTemplate(
      String groupName,
      String groupImageUrl,
      String groupCode,
      String leaderEmail
  ) {

    Context context = new Context();

    context.setVariable("groupName", groupName);
    context.setVariable("groupImage", groupImageUrl);
    context.setVariable("groupCode", groupCode);
    context.setVariable("leaderEmail", leaderEmail);

    return templateEngine.process(
        "groups/member-removed-email",
        context
    );
  }

  public String buildGroupDeletedEmailTemplate(
      String groupName,
      String groupDescription,
      String groupCode,
      String leaderEmail
  ) {

    Context context = new Context();

    context.setVariable("groupName", groupName);
    context.setVariable("groupDescription", groupDescription);
    context.setVariable("groupCode", groupCode);
    context.setVariable("leaderEmail", leaderEmail);

    return templateEngine.process(
        "groups/group-deleted-email",
        context
    );
  }
}