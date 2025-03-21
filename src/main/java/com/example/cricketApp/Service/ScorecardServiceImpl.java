package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.Player;
import com.example.cricketApp.Entity.PlayerType;
import com.example.cricketApp.Entity.Team;
import com.example.cricketApp.Repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScorecardServiceImpl implements ScorecardService {

    @Autowired
    private PlayerRepository playerRepository;

    public void printInningBattingScorecard(int matchId, Team team) {
        List<Player> players = playerRepository.findByTeamId(team.getTeamId());
        System.out.println("=======================================");
        System.out.printf("%-20s %-10s %-10s%n", "Player", "Runs", "Balls");
        System.out.println("=======================================");
        for (Player p : players) {
            if (PlayerMatchPerformanceServiceImpl.playerStatsMap.get(matchId).get(p.getPlayerId()) == null) {
                break;
            }
            int runs = PlayerMatchPerformanceServiceImpl.playerStatsMap.get(matchId).get(p.getPlayerId()).getRunsScored();
            int balls = PlayerMatchPerformanceServiceImpl.playerStatsMap.get(matchId).get(p.getPlayerId()).getBallsFaced();
            System.out.printf("%-20s %-10d %-10d%n", p.getPlayerName(), runs, balls);
        }
        System.out.println("=======================================\n");
    }

    public void printInningBowlingScorecard(int matchId, Team team) {
        List<Player> players = playerRepository.findByTeamId(team.getTeamId())
                .stream().filter(p -> p.getPlayerType() == PlayerType.BOWLER).toList();

        System.out.println("==========================================");
        System.out.printf("%-20s %-10s %-10s%n", "Player", "Overs", "Wickets");
        System.out.println("==========================================");

        for (Player p : players) {
            if (PlayerMatchPerformanceServiceImpl.playerStatsMap.get(matchId).get(p.getPlayerId()) == null) {
                break;
            }
            int balls = PlayerMatchPerformanceServiceImpl.playerStatsMap.get(matchId).get(p.getPlayerId()).getBallsBowled();
            float over;
            if (balls % 6 == 0) {
                over = (float) balls / 6;
            } else {
                int rem = balls % 6;
                over = balls / 6 + (float) rem / 10;
            }
            int wickets = PlayerMatchPerformanceServiceImpl.playerStatsMap.get(matchId).get(p.getPlayerId()).getWicketsTaken();
            System.out.printf("%-20s %-10.1f %-10d%n", p.getPlayerName(), over, wickets);
        }
        System.out.println("==========================================\n");
    }

}
