package com.collabrium.groups.management.application.internal.ports;

import com.collabrium.groups.shared.infrastructure.clients.iam.resources.UserOnlyResource;

public interface IamQueryPort {

  UserOnlyResource getUserOnlyById(Long id);
}