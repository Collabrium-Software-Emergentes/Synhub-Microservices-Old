package com.collabrium.groups.management.infrastructure.adapters;

import com.collabrium.groups.management.application.internal.ports.IamQueryPort;
import com.collabrium.groups.shared.infrastructure.clients.iam.IamFeignClient;
import com.collabrium.groups.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.springframework.stereotype.Component;

@Component
public class IamQueryAdapter implements IamQueryPort {

  private final IamFeignClient client;

  public IamQueryAdapter(
      IamFeignClient client
  ) {

    this.client = client;
  }

  @Override
  public UserOnlyResource getUserOnlyById(Long id) {
    return client.getUserOnlyById(id);
  }
}