package com.collabrium.groups.management.infrastructure.persistence.jpa.repositories;

import com.collabrium.groups.management.domain.model.aggregates.Invitation;
import com.collabrium.groups.management.domain.model.valueobjects.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

  Optional<Invitation> findByMemberId(MemberId memberId);

  List<Invitation> findByGroup_Id(Long groupId);

  boolean existsByMemberId(MemberId memberId);
}