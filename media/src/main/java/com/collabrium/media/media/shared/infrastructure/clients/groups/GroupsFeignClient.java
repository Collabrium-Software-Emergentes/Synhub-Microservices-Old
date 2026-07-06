package com.collabrium.media.media.shared.infrastructure.clients.groups;

import com.collabrium.media.media.shared.infrastructure.clients.groups.resources.GroupOnlyResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "groups-service")
public interface GroupsFeignClient {

  @GetMapping("/api/v1/groups/{groupId}")
  GroupOnlyResource getGroupById(
      @PathVariable Long groupId
  );
}