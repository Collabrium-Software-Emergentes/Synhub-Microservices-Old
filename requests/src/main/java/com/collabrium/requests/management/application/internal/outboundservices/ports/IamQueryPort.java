package com.collabrium.requests.management.application.internal.outboundservices.ports;

import com.collabrium.requests.shared.infrastructure.clients.iam.resources.UserOnlyResource;

public interface IamQueryPort {

  UserOnlyResource getUserOnlyById(Long id);
}