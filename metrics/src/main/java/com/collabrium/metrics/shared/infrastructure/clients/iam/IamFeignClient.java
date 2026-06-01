package com.collabrium.metrics.shared.infrastructure.clients.iam;

import com.collabrium.metrics.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "iam-service")
public interface IamFeignClient {

  @GetMapping(value = "/api/v1/users", params = "memberId")
  UserOnlyResource getUserOnlyByMemberId(
      @RequestParam Long memberId
  );
}