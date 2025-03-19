package com.example.cricketApp.Repository;

import com.example.cricketApp.Entity.PlayerMatchWiseStats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PlayerMatchPerformanceRepository extends MongoRepository<PlayerMatchWiseStats, String> {

    List<PlayerMatchWiseStats> findByPlayerId(int playerId);

    List<PlayerMatchWiseStats> findByMatchId(int matchId);
}
