package com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories;

import com.collabrium.tasks.management.domain.model.aggregates.Task;
import com.collabrium.tasks.management.domain.model.valueobjects.GroupId;
import com.collabrium.tasks.management.domain.model.valueobjects.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  List<Task> findByMember_Id(Long memberId);

  List<Task> findByStatus(TaskStatus status);

  List<Task> findByGroupId(GroupId groupId);

  void deleteAllByMember_Id(Long memberId);

  void deleteAllByGroupId(GroupId groupId);
}