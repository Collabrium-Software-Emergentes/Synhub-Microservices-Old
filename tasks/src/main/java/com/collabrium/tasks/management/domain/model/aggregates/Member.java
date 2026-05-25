package com.collabrium.tasks.management.domain.model.aggregates;

import com.collabrium.tasks.management.domain.exceptions.InvalidMemberException;
import com.collabrium.tasks.management.domain.model.commands.CreateMemberCommand;
import com.collabrium.tasks.management.domain.model.valueobjects.GroupId;
import com.collabrium.tasks.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "members")
@NoArgsConstructor
@AttributeOverrides({
    @AttributeOverride(
        name = "groupId.value",
        column = @Column(name = "group_id")
    )
})
public class Member extends AuditableAbstractAggregateRoot<Member> {

  @OneToMany(
      mappedBy = "member",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY
  )
  private List<Task> tasks = new ArrayList<>();

  @Embedded
  private GroupId groupId;

  public Member(CreateMemberCommand command) {
    validateCreation(command);
    this.groupId = null;
  }

  private void validateCreation(CreateMemberCommand command) {

    if (command == null) {
      throw InvalidMemberException.forNullCreateCommand();
    }
  }

  public void assignGroup(GroupId groupId) {

    if (groupId == null) {
      throw InvalidMemberException.forNullGroupId();
    }

    this.groupId = groupId;
  }

  public void removeGroup() {
    this.groupId = null;
  }

  public boolean belongsToGroup() {
    return this.groupId != null;
  }

  public void addTask(Task task) {

    validateTask(task);

    this.tasks.add(task);
    task.setMember(this);
  }

  public void removeTask(Task task) {

    validateTask(task);

    this.tasks.remove(task);
    task.setMember(null);
  }

  public void clearTasks() {

    if (this.tasks.isEmpty()) {
      return;
    }

    for (Task task : this.tasks) {
      task.setMember(null);
    }

    this.tasks.clear();
  }

  private void validateTask(Task task) {

    if (task == null) {
      throw InvalidMemberException.forNullTask();
    }
  }

  @Override
  public String toString() {

    return String.format(
        "Member{id=%d, groupId=%s, tasks=%d}",
        getId() != null ? getId() : 0,
        groupId != null ? groupId.toString() : "null",
        tasks.size()
    );
  }
}