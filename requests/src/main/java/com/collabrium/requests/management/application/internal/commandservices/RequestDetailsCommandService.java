package com.collabrium.requests.management.application.internal.commandservices;

import com.collabrium.requests.management.infrastructure.persistence.jpa.repositories.RequestRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestDetailsCommandService {

  private final RequestRepository requestRepository;

  public RequestDetailsCommandService(
      RequestRepository requestRepository
  ) {

    this.requestRepository = requestRepository;
  }
}