package com.collabrium.media.media.shared.interfaces.rest.resources;

import java.util.List;

public record AuthenticatedUser(
    Long userId,
    String username,
    List<String> roles
) {
}