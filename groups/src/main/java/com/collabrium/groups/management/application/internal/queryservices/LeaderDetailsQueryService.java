package com.collabrium.groups.management.application.internal.queryservices;

import com.collabrium.groups.management.application.internal.ports.IamQueryPort;
import com.collabrium.groups.management.application.internal.dto.LeaderDetailsDTO;
import com.collabrium.groups.management.domain.model.queries.GetLeaderDetailsByUserIdQuery;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.LeaderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LeaderDetailsQueryService {

  private final LeaderRepository leaderRepository;
  private final IamQueryPort iamQueryPort;

  public LeaderDetailsQueryService(
      LeaderRepository leaderRepository,
      IamQueryPort iamQueryPort
  ) {

    this.leaderRepository = leaderRepository;
    this.iamQueryPort = iamQueryPort;
  }

  public Optional<LeaderDetailsDTO> handle(GetLeaderDetailsByUserIdQuery query) {

    var user = iamQueryPort.getUserOnlyById(query.userId());

    if (user == null || user.leaderId() == null) {
      return Optional.empty();
    }

    var optionalLeader = leaderRepository.findById(user.leaderId());

    return optionalLeader.map(leader ->
        new LeaderDetailsDTO(
            leader.getId(),
            user.username(),
            user.name(),
            user.surname(),
            user.imgUrl(),
            user.email(),
            leader.getFormattedAverageSolutionTime(),
            leader.getSolvedRequests()
        )
    );
  }
}
