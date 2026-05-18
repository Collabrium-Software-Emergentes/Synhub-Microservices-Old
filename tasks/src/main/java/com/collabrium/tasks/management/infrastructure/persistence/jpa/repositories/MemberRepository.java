package com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories;

import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.domain.model.valueobjects.GroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  List<Member> findMembersByGroupId(GroupId groupId);
}