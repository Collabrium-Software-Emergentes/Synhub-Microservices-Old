package com.collabrium.groups.management.domain.services;

import com.collabrium.groups.management.domain.model.aggregates.Invitation;
import com.collabrium.groups.management.domain.model.queries.GetInvitationByMemberIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetInvitationsByGroupIdQuery;

import java.util.List;
import java.util.Optional;

public interface InvitationQueryService {

  Optional<Invitation> handle(GetInvitationByMemberIdQuery query);

  List<Invitation> handle(GetInvitationsByGroupIdQuery query);
}
