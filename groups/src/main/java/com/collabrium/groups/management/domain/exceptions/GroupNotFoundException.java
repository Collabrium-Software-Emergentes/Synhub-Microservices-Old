package com.collabrium.groups.management.domain.exceptions;

public class GroupNotFoundException extends RuntimeException {

  public GroupNotFoundException(Long leaderId) {
    super("Group not found for leader id: " + leaderId);
  }

  public static GroupNotFoundException forLeader(Long leaderId) {
    return new GroupNotFoundException(leaderId);
  }
}
