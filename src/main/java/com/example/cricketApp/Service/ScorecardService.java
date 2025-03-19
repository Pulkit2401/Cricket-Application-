package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.Team;

public interface ScorecardService {

    void printInningBattingScorecard(int matchId, Team team);

    void printInningBowlingScorecard(int matchId, Team team);
}
