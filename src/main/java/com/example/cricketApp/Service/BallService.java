package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.Ball;
import com.example.cricketApp.Entity.Inning;
import com.example.cricketApp.Entity.Player;
import org.bson.types.ObjectId;

public interface BallService {

    boolean startBall(int ballOutcome, int i, Player striker, Player nonStriker, Player bowler, int wicketCount, int matchId, Inning inning1, Inning inning2, ObjectId overId);

    void addBallToBuffer(int ballNumber, ObjectId overId, int matchId, ObjectId inningId, int bowlerId, int strikerId, int nonStrikerId, int runsScored, int teamScore, int wicketsFallen, boolean isWicket);

    void saveBallsAfterOver();
}