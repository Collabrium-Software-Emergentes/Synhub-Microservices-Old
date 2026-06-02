package com.collabrium.metrics.management.infrastructure.adapters;

import com.collabrium.metrics.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.metrics.shared.infrastructure.clients.iam.IamFeignClient;
import com.collabrium.metrics.shared.infrastructure.clients.iam.resources.UserOnlyResource;
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
  public UserOnlyResource getUserOnlyById(Long userId) {
    return client.getUserOnlyById(userId);
  }

  @Override
  public UserOnlyResource getUserOnlyByMemberId(Long id) {
    return client.getUserOnlyByMemberId(id);
  }
}