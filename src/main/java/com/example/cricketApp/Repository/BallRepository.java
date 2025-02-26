package com.example.cricketApp.Repository;

import com.example.cricketApp.Entity.Ball;
import com.example.cricketApp.Entity.Over;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BallRepository extends MongoRepository <Ball, ObjectId> {
}
