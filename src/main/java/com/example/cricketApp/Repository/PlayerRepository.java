package com.example.cricketApp.Repository;

import com.example.cricketApp.Entity.Player;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends MongoRepository<Player, Integer> {

    List<Player> findByTeamId(int teamId);
}

