package com.collabrium.requests.management.infrastructure.adapters;

import com.collabrium.requests.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.requests.shared.infrastructure.clients.iam.IamFeignClient;
import com.collabrium.requests.shared.infrastructure.clients.iam.resources.UserOnlyResource;
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