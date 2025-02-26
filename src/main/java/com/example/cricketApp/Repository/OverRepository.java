package com.example.cricketApp.Repository;

import com.example.cricketApp.Entity.Over;
import com.example.cricketApp.Entity.Team;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OverRepository extends MongoRepository <Over, ObjectId> {
}
