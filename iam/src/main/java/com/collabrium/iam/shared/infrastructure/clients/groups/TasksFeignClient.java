package com.collabrium.iam.shared.infrastructure.clients.groups;

import com.collabrium.iam.shared.infrastructure.clients.groups.resources.MemberResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tasks-service")
public interface TasksFeignClient {

  @GetMapping("/api/v1/member/{memberId}")
  MemberResource getMemberById(
      @PathVariable Long memberId
  );
}