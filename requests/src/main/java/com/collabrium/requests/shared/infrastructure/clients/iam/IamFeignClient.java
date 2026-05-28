package com.collabrium.requests.shared.infrastructure.clients.iam;

import com.collabrium.requests.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "iam-service")
public interface IamFeignClient {

  @GetMapping("/api/v1/users/{userId}/domain-profile")
  UserOnlyResource getUserOnlyById(
      @PathVariable Long userId
  );
}