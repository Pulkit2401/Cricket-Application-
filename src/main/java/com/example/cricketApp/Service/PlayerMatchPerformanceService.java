package com.example.cricketApp.Service;
import com.example.cricketApp.Entity.Player;
import com.example.cricketApp.Entity.PlayerMatchWiseStats;
import com.example.cricketApp.Repository.PlayerMatchPerformanceRepository;
import com.example.cricketApp.Repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PlayerMatchPerformanceService {
    @Autowired
    private PlayerMatchPerformanceRepository playerMatchPerformanceRepository;
    @Autowired
    private PlayerRepository playerRepository;

    public static Map<Integer, PlayerMatchWiseStats> playerStatsMap = new HashMap<>();

    public PlayerMatchWiseStats updatePlayerMatchPerformance(int matchId, int playerId, int runsScored, int sixes, int fours, int wicketsTaken, int ballsFaced, int ballsBowled, int runsGiven) {
        PlayerMatchWiseStats performance = playerStatsMap.getOrDefault(playerId, new PlayerMatchWiseStats(matchId, playerId));
        performance.setRunsScored(runsScored != 0 ? performance.getRunsScored() + runsScored : performance.getRunsScored());
        performance.setSixes( sixes != 0 ? performance.getSixes() + sixes : performance.getSixes());
        performance.setFours(fours != 0 ? performance.getFours() + fours : performance.getFours());
        performance.setWicketsTaken(wicketsTaken != 0 ? performance.getWicketsTaken() + wicketsTaken : performance.getWicketsTaken());
        performance.setBallsFaced(ballsFaced != 0 ? performance.getBallsFaced() + ballsFaced : performance.getBallsFaced());
        performance.setRunsGiven(runsGiven != 0 ? performance.getRunsGiven() + runsGiven : performance.getRunsGiven());
        performance.setBallsBowled(ballsBowled != 0 ? performance.getBallsBowled() + ballsBowled : performance.getBallsBowled());
        playerStatsMap.put(playerId, performance);
        return performance;
    }

    public void savePlayerStatsAfterMatch() {
        playerMatchPerformanceRepository.saveAll(playerStatsMap.values());
    }

    public void updatePlayerFinalStats(){
        for(int playerId : playerStatsMap.keySet()){
            Player player = playerRepository.findById(playerId).get();
            if(playerStatsMap.get(playerId).getRunsScored()>=100){
                player.setHundreds(player.getHundreds() + 1);
            }
            else if(playerStatsMap.get(playerId).getRunsScored()>=50 && playerStatsMap.get(playerId).getRunsScored()<100){
                player.setFifties(player.getFifties() + 1);
            }

            if(playerStatsMap.get(playerId).getWicketsTaken()>=5){
                player.setFiveWicketHauls(player.getFiveWicketHauls() + 1);
            }
            player.setRunsScored(player.getRunsScored() + playerStatsMap.get(playerId).getRunsScored());
            player.setWicketsTaken(player.getWicketsTaken() + playerStatsMap.get(playerId).getWicketsTaken());
            player.setBallsPlayed(player.getBallsPlayed() + playerStatsMap.get(playerId).getBallsBowled());
            playerRepository.save(player);
        }
        playerStatsMap.clear();
    }
}
