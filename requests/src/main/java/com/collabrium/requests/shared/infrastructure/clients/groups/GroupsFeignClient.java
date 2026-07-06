package com.collabrium.requests.shared.infrastructure.clients.groups;

import com.collabrium.requests.shared.infrastructure.clients.groups.resources.GroupOnlyResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "groups-service")
public interface GroupsFeignClient {

  @GetMapping("/api/v1/groups")
  GroupOnlyResource getGroupOnlyByLeaderId(
      @RequestParam Long leaderId
  );

  @GetMapping("/api/v1/groups/{groupId}")
  GroupOnlyResource getGroupOnlyByGroupId(
      @PathVariable Long groupId
  );
}