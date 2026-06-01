package com.collabrium.metrics.shared.infrastructure.clients.groups;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "groups-service")
public interface GroupsFeignClient {
}