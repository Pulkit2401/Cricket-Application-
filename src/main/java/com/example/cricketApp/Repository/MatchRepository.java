package com.example.cricketApp.Repository;

import com.example.cricketApp.Entity.Match;
import com.example.cricketApp.Entity.Team;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends MongoRepository <Match, Integer> {
}
