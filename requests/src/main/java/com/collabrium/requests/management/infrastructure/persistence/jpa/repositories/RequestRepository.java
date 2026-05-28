package com.collabrium.requests.management.infrastructure.persistence.jpa.repositories;

import com.collabrium.requests.management.domain.model.aggregates.Request;
import com.collabrium.requests.management.domain.model.valueobjects.TaskId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

  List<Request> findByTaskId(TaskId taskId);
}