package com.collabrium.groups.management.domain.model.aggregates;

import com.collabrium.groups.management.domain.exceptions.InvalidGroupException;
import com.collabrium.groups.management.domain.model.commands.UpdateGroupCommand;
import com.collabrium.groups.management.domain.model.valueobjects.GroupCode;
import com.collabrium.groups.management.domain.model.valueobjects.ImgUrl;
import com.collabrium.groups.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@Setter
@Getter
public class Group extends AuditableAbstractAggregateRoot<Group> {

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "code", unique = true, nullable = false))
  private GroupCode code;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  private String description;

  @Embedded
  @AttributeOverride(name = "imgUrl", column = @Column(name = "img_url"))
  private ImgUrl imgUrl;

  @OneToOne
  @JoinColumn(name = "leader_id", nullable = false)
  private Leader leader;

  @Column(name = "member_count", nullable = false)
  private Integer memberCount;

  @Column(name = "public_id")
  private String publicId;

  public Group(
      String name,
      String description,
      String imgUrl,
      Leader leader,
      GroupCode code
  ) {

    validateGroupCreation(name, description, leader, code);
    this.name = name;
    this.description = description;
    this.imgUrl = imgUrl != null && !imgUrl.isBlank() ? new ImgUrl(imgUrl) : null;
    this.leader = leader;
    this.memberCount = 0;
    this.code = code;
    this.publicId = null;
  }

  public Group(
      String name,
      String description,
      String imgUrl,
      String publicId,
      Leader leader,
      GroupCode code
  ) {
    validateGroupCreation(name, description, leader, code);

    this.name = name;
    this.description = description;
    this.imgUrl =
        imgUrl != null && !imgUrl.isBlank()
            ? new ImgUrl(imgUrl)
            : null;
    this.publicId = publicId;
    this.leader = leader;
    this.memberCount = 0;
    this.code = code;
  }

  private void validateGroupCreation(String name, String description, Leader leader, GroupCode code) {
    if (name == null) {
      throw InvalidGroupException.forNullName();
    }
    if (name.isBlank()) {
      throw InvalidGroupException.forEmptyName();
    }
    if (description == null) {
      throw InvalidGroupException.forNullDescription();
    }
    if (description.isBlank()) {
      throw InvalidGroupException.forEmptyDescription();
    }
    if (leader == null) {
      throw InvalidGroupException.forNullLeader();
    }
    if (code == null) {
      throw InvalidGroupException.forNullCode();
    }
  }

  public void updateInformation(UpdateGroupCommand command) {

    updateName(command.name());
    updateDescription(command.description());
  }

  public void increaseMemberCount() {
    validateMemberCountOperation();
    this.memberCount++;
  }

  public void decreaseMemberCount() {
    validateMemberCountOperation();
    if (this.memberCount <= 0) {
      throw InvalidGroupException.forMemberCountDecrementWhenZero();
    }
    this.memberCount--;
  }

  private void validateMemberCountOperation() {
    if (this.memberCount == null) {
      this.memberCount = 0;
    }
    if (this.memberCount < 0) {
      throw InvalidGroupException.forInvalidMemberCount();
    }
  }

  private void updateName(String name) {

    if (name == null) {
      return;
    }

    validateText(name, InvalidGroupException.forEmptyName());

    this.name = name;
  }

  private void updateDescription(String description) {

    if (description == null) {
      return;
    }

    validateText(
        description,
        InvalidGroupException.forEmptyDescription()
    );

    this.description = description;
  }

  private void updateImgUrl(String imgUrl) {

    if (imgUrl == null) {
      return;
    }

    this.imgUrl =
        imgUrl.isBlank()
            ? null
            : new ImgUrl(imgUrl);
  }

  private void validateText(
      String value,
      RuntimeException exception
  ) {

    if (value.isBlank()) {
      throw exception;
    }
  }

  public void updateImage(
      String imageUrl,
      String publicId
  ) {

    this.imgUrl =
        imageUrl == null || imageUrl.isBlank()
            ? null
            : new ImgUrl(imageUrl);

    this.publicId = publicId;
  }

  @Override
  public String toString() {
    return String.format("Group{id=%d, code=%s, name=%s, memberCount=%d, leaderId=%d}",
        getId() != null ? getId() : 0,
        code != null ? code.toString() : "null",
        name,
        memberCount != null ? memberCount : 0,
        leader != null && leader.getId() != null ? leader.getId() : 0
    );
  }
}