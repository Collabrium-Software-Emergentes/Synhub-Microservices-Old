package com.collabrium.metrics.management.application.internal.outboundservices.ports;

import com.collabrium.metrics.shared.infrastructure.clients.iam.resources.UserOnlyResource;

public interface IamQueryPort {

  UserOnlyResource getUserOnlyByMemberId(Long id);
}