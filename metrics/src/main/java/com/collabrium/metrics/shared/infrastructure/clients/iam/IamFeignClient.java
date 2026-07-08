package com.collabrium.metrics.shared.infrastructure.clients.iam;

import com.collabrium.metrics.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

//Ojito y muy importante: Se tiene que cambiar o eliminar el url si vas
// a correrlo en el docker o en un entorno distinto o en otro puerto de tu localhost
@FeignClient(name = "iam-service", url="http://localhost:8081")
public interface IamFeignClient {

  @GetMapping("/api/v1/users/{userId}/domain-profile")
  UserOnlyResource getUserOnlyById(
    @PathVariable Long userId
  );

  @GetMapping(value = "/api/v1/users", params = "memberId")
  UserOnlyResource getUserOnlyByMemberId(
      @RequestParam Long memberId
  );
}