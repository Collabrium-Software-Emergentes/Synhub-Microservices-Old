package com.collabrium.iam.authentication.domain.services;

import com.collabrium.iam.authentication.domain.model.aggregates.User;
import com.collabrium.iam.authentication.domain.model.queries.GetAllUsersQuery;
import com.collabrium.iam.authentication.domain.model.queries.GetUserByIdQuery;
import com.collabrium.iam.authentication.domain.model.queries.GetUserByUsernameQuery;
import com.collabrium.iam.authentication.domain.model.queries.GetUserByMemberIdQuery;
import com.collabrium.iam.authentication.domain.model.queries.GetUserByLeaderIdQuery;
import com.collabrium.iam.authentication.domain.model.queries.GetUserLeaderByIdQuery;
import com.collabrium.iam.authentication.domain.model.queries.GetUserMemberByIdQuery;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {

  List<User> handle(GetAllUsersQuery query);

  Optional<User> handle(GetUserByIdQuery query);

  Optional<User> handle(GetUserByUsernameQuery query);

  Optional<User> handle(GetUserByMemberIdQuery query);

  Optional<User> handle(GetUserByLeaderIdQuery query);

//  Optional<UserWithLeaderResource> handle(GetUserLeaderByIdQuery query);
//
//  Optional<UserWithMemberInfo> handle(GetUserMemberByIdQuery query);
}
