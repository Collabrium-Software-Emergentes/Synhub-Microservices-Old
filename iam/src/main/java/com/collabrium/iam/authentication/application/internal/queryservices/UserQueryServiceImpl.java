package com.collabrium.iam.authentication.application.internal.queryservices;

import com.collabrium.iam.authentication.domain.model.aggregates.User;
import com.collabrium.iam.authentication.domain.model.queries.GetAllUsersQuery;
import com.collabrium.iam.authentication.domain.model.queries.GetUserByIdQuery;
import com.collabrium.iam.authentication.domain.model.queries.GetUserByLeaderIdQuery;
import com.collabrium.iam.authentication.domain.model.queries.GetUserByMemberIdQuery;
import com.collabrium.iam.authentication.domain.model.queries.GetUserByUsernameQuery;
import com.collabrium.iam.authentication.domain.model.valueobjects.LeaderId;
import com.collabrium.iam.authentication.domain.model.valueobjects.MemberId;
import com.collabrium.iam.authentication.domain.services.UserQueryService;
import com.collabrium.iam.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserQueryService} interface.
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {
  private final UserRepository userRepository;
//  private final GroupsServiceClient groupsServiceClient;
//  private final TasksServiceClient tasksServiceClient;

  /**
   * Constructor.
   *
   * @param userRepository {@link UserRepository} instance.
   */
  public UserQueryServiceImpl(UserRepository userRepository
//                              GroupsServiceClient groupsServiceClient,
//                              TasksServiceClient tasksServiceClient
  ) {
    this.userRepository = userRepository;
//    this.groupsServiceClient = groupsServiceClient;
//    this.tasksServiceClient = tasksServiceClient;
  }

  /**
   * This method is used to handle {@link GetAllUsersQuery} query.
   * @param query {@link GetAllUsersQuery} instance.
   * @return {@link List} of {@link User} instances.
   * @see GetAllUsersQuery
   */
  @Override
  public List<User> handle(GetAllUsersQuery query) {
    return userRepository.findAll();
  }

  /**
   * This method is used to handle {@link GetUserByIdQuery} query.
   * @param query {@link GetUserByIdQuery} instance.
   * @return {@link Optional} of {@link User} instance.
   * @see GetUserByIdQuery
   */
  @Override
  public Optional<User> handle(GetUserByIdQuery query) {
    return userRepository.findById(query.userId());
  }

  /**
   * This method is used to handle {@link GetUserByUsernameQuery} query.
   * @param query {@link GetUserByUsernameQuery} instance.
   * @return {@link Optional} of {@link User} instance.
   * @see GetUserByUsernameQuery
   */
  @Override
  public Optional<User> handle(GetUserByUsernameQuery query) {
    return userRepository.findByUsername(query.username());
  }

  /**
   * This method is used to handle {@link GetUserByMemberIdQuery} query.
   * @param query {@link GetUserByMemberIdQuery} instance.
   * @return {@link Optional} of {@link User} instance.
   * @see GetUserByMemberIdQuery
   */
  @Override
  public Optional<User> handle(GetUserByMemberIdQuery query) {

    MemberId memberId = new MemberId(query.memberId());

    return userRepository.findByMemberId(memberId);
  }

  /**
   * This method is used to handle {@link GetUserByLeaderIdQuery} query.
   * @param query {@link GetUserByLeaderIdQuery} instance.
   * @return {@link Optional} of {@link User} instance.
   * @see GetUserByLeaderIdQuery
   */
  @Override
  public Optional<User> handle(GetUserByLeaderIdQuery query) {

    LeaderId leaderId = new LeaderId(query.leaderId());

    return userRepository.findByLeaderId(leaderId);
  }
}