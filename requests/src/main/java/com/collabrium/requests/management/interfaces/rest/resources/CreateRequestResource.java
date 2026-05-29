package com.collabrium.requests.management.interfaces.rest.resources;

public record CreateRequestResource(
    String description,
    String requestType
) {
}