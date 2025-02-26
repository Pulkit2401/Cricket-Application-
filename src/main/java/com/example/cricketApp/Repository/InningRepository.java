package com.example.cricketApp.Repository;

import com.example.cricketApp.Entity.Inning;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InningRepository extends MongoRepository <Inning, ObjectId> {
    public List<Inning> findByMatchId(int matchId);
}
