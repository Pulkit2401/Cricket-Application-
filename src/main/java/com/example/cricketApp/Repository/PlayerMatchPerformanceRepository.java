package com.example.cricketApp.Repository;

import com.example.cricketApp.Entity.PlayerMatchId;
import com.example.cricketApp.Entity.PlayerMatchWiseStats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerMatchPerformanceRepository extends MongoRepository<PlayerMatchWiseStats, String> {
   public List<PlayerMatchWiseStats> findByPlayerId (int playerId);
   public List<PlayerMatchWiseStats> findByMatchId (int matchId);
}
