package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.PlayerMatchWiseStats;

public interface PlayerMatchPerformanceService {

    PlayerMatchWiseStats updatePlayerMatchPerformance(int matchId, int playerId, int runsScored, int sixes, int fours, int wicketsTaken, int ballsFaced, int ballsBowled, int runsGiven);

    void savePlayerStatsAfterMatch(int matchId);

    void updatePlayerFinalStats(int matchId);
}