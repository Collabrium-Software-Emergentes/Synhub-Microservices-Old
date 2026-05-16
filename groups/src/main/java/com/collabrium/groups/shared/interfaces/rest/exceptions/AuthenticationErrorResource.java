package com.collabrium.groups.shared.interfaces.rest.exceptions;

import java.util.List;

public record AuthenticationErrorResource(
    String timestamp,
    int status,
    String error,
    String code,
    String message,
    String path,
    List<String> details
) {
}