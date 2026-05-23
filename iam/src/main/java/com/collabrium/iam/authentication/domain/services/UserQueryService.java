package com.collabrium.iam.authentication.domain.services;

import com.collabrium.iam.authentication.domain.model.aggregates.User;
import com.collabrium.iam.authentication.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {

  List<User> handle(GetAllUsersQuery query);

  Optional<User> handle(GetUserOnlyByIdQuery query);

  Optional<User> handle(GetUserByUsernameQuery query);

  Optional<User> handle(GetUserByMemberIdQuery query);

  Optional<User> handle(GetUserByLeaderIdQuery query);

//  Optional<UserWithLeaderResource> handle(GetUserLeaderByIdQuery query);
//
//  Optional<UserWithMemberInfo> handle(GetUserMemberByIdQuery query);
}
