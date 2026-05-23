package com.collabrium.tasks.management.application.internal.outboundservices.ports;

import com.collabrium.tasks.shared.infrastructure.clients.iam.resources.UserOnlyResource;

public interface IamQueryPort {

  UserOnlyResource getUserOnlyById(Long id);

  UserOnlyResource getUserByMemberId(Long memberId);
}