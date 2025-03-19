package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.Player;
import com.example.cricketApp.Entity.PlayerMatchWiseStats;
import com.example.cricketApp.Repository.PlayerMatchPerformanceRepository;
import com.example.cricketApp.Repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlayerMatchPerformanceServiceImpl implements PlayerMatchPerformanceService {
    @Autowired
    private PlayerMatchPerformanceRepository playerMatchPerformanceRepository;
    @Autowired
    private PlayerRepository playerRepository;
    public static Map<Integer, Map<Integer, PlayerMatchWiseStats>> playerStatsMap = new ConcurrentHashMap<>();

    public PlayerMatchWiseStats updatePlayerMatchPerformance(int matchId, int playerId, int runsScored, int sixes, int fours, int wicketsTaken, int ballsFaced, int ballsBowled, int runsGiven) {
        playerStatsMap.putIfAbsent(matchId, new ConcurrentHashMap<>());
        Map<Integer, PlayerMatchWiseStats> matchStats = playerStatsMap.get(matchId);
        PlayerMatchWiseStats performance = matchStats.computeIfAbsent(playerId,
                k -> new PlayerMatchWiseStats(matchId, playerId));

        synchronized (performance) {
            performance.setRunsScored(runsScored != 0 ? performance.getRunsScored() + runsScored : performance.getRunsScored());
            performance.setSixes(sixes != 0 ? performance.getSixes() + sixes : performance.getSixes());
            performance.setFours(fours != 0 ? performance.getFours() + fours : performance.getFours());
            performance.setWicketsTaken(wicketsTaken != 0 ? performance.getWicketsTaken() + wicketsTaken : performance.getWicketsTaken());
            performance.setBallsFaced(ballsFaced != 0 ? performance.getBallsFaced() + ballsFaced : performance.getBallsFaced());
            performance.setRunsGiven(runsGiven != 0 ? performance.getRunsGiven() + runsGiven : performance.getRunsGiven());
            performance.setBallsBowled(ballsBowled != 0 ? performance.getBallsBowled() + ballsBowled : performance.getBallsBowled());
        }

        return performance;
    }

    public void savePlayerStatsAfterMatch(int matchId) {
        Map<Integer, PlayerMatchWiseStats> matchStats = playerStatsMap.get(matchId);
        if (matchStats != null) {
            synchronized (matchStats) {
                playerMatchPerformanceRepository.saveAll(matchStats.values());
            }
        }
    }

    public void updatePlayerFinalStats(int matchId) {
        Map<Integer, PlayerMatchWiseStats> matchStats = playerStatsMap.get(matchId);
        if (matchStats != null) {
            synchronized (matchStats) {
                for (int playerId : matchStats.keySet()) {
                    PlayerMatchWiseStats stats = matchStats.get(playerId);
                    Player player = playerRepository.findById(playerId).orElse(null);

                    if (player != null) {
                        if (stats.getRunsScored() >= 100) {
                            player.setHundreds(player.getHundreds() + 1);
                        } else if (stats.getRunsScored() >= 50) {
                            player.setFifties(player.getFifties() + 1);
                        }
                        if (stats.getWicketsTaken() >= 5) {
                            player.setFiveWicketHauls(player.getFiveWicketHauls() + 1);
                        }

                        player.setRunsScored(player.getRunsScored() + stats.getRunsScored());
                        player.setWicketsTaken(player.getWicketsTaken() + stats.getWicketsTaken());
                        player.setBallsPlayed(player.getBallsPlayed() + stats.getBallsBowled());

                        playerRepository.save(player);
                    }
                }
                playerStatsMap.remove(matchId);
            }
        }
    }

}
