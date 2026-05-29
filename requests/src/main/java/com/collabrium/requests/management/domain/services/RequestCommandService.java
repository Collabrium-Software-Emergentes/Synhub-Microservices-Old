package com.collabrium.requests.management.domain.services;

import com.collabrium.requests.management.domain.model.aggregates.Request;
import com.collabrium.requests.management.domain.model.commands.UpdateRequestStatusCommand;

import java.util.Optional;

public interface RequestCommandService {

  Optional<Request> handle(UpdateRequestStatusCommand command);
}