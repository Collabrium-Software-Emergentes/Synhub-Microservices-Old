package com.collabrium.metrics.shared.infrastructure.clients.groups.resources;

public record GroupOnlyResource(
  Long id,
  String name,
  String imgUrl,
  String description,
  String code,
  Long leaderId
) {
}