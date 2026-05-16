package com.collabrium.iam.authentication.domain.services;

import com.collabrium.iam.authentication.domain.model.entities.Role;
import com.collabrium.iam.authentication.domain.model.queries.GetAllRolesQuery;
import com.collabrium.iam.authentication.domain.model.queries.GetRoleByNameQuery;

import java.util.List;
import java.util.Optional;

public interface RoleQueryService {

  List<Role> handle(GetAllRolesQuery query);

  Optional<Role> handle(GetRoleByNameQuery query);
}
