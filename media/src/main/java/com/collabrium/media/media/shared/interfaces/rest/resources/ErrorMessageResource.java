package com.collabrium.media.media.shared.interfaces.rest.resources;

public record ErrorMessageResource(
    String timestamp,
    int status,
    String error,
    String code,
    String message,
    String path) {
}