package com.example.cricketApp.Service;

import org.bson.types.ObjectId;

public interface OverService {

    ObjectId createOver(int overNumber, int playerBowling, int matchId, ObjectId inningId);
}
