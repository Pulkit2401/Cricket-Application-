package com.example.cricketApp.Service;//package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.*;
import com.example.cricketApp.Repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;

@Service
public class PlayGameService {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private InningRepository inningRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private OverService overService;
    @Autowired
    private BallService ballService;
    @Autowired
    private PlayerMatchPerformanceService playerMatchPerformanceService;
    @Autowired
    private PlayerMatchPerformanceRepository playerMatchPerformanceRepository;

    public void play(Match match, Inning inning1, Inning inning2, LinkedHashSet<Integer> setPlayers) {
        int matchId = match.getMatchId();
        ObjectId inningId1 = inning1.getInningId();
        int wicketCount = 0;
        int team1Id = inning1.getTeamBattingId();
        int team2Id = inning1.getTeamBowlingId();

        List<Player> team1Players = playerRepository.findByTeamId(team1Id);
        List<Player> team2Players = playerRepository.findByTeamId(team2Id);
        List<Player> bowlers = team2Players.stream()
                .filter(p -> p.getPlayerType() == PlayerType.BOWLER)
                .toList();
        int numberOfOvers = match.getOvers();
        int overCount = 1;
        Player striker = team1Players.get(0);
        Player nonStriker = team1Players.get(1);
        setPlayers.add(striker.getPlayerId());
        setPlayers.add(nonStriker.getPlayerId());

        while (overCount <= numberOfOvers) {
            inning1.setOversBowled(overCount);
            System.out.println("over number : " + overCount);
            Player bowler = bowlers.get((overCount - 1) % bowlers.size());
            int bowlerId = bowler.getPlayerId();
            System.out.println(bowler.getPlayerName() + " is bowling " + overCount + " over\n");
            ObjectId overId = overService.createOver(overCount, bowlerId, matchId, inningId1);

            for (int i = 0; i < 6; i++) {
                int ballOutcome = ballResult();
                boolean ball = ballService.startBall(ballOutcome, i, striker, nonStriker, bowler, wicketCount, matchId, inning1, inning2, overId);
                if(ball){
                    ballService.saveBallsAfterOver();
                    inningRepository.save(inning1);
                    playerMatchPerformanceService.savePlayerStatsAfterMatch();
                    return;
                }
                if(ballOutcome==7){
                    for(Player p : team1Players){
                        if(!setPlayers.contains(p.getPlayerId())){
                            setPlayers.add(p.getPlayerId());
                            striker=p;
                            System.out.println("Next batsman coming: " + striker.getPlayerName());
                            break;
                        }
                    }
                }
                if(ballOutcome % 2 ==1 && ballOutcome!=7){
                    Player temp = striker;
                    striker = nonStriker;
                    nonStriker = temp;
                }
            }
            ballService.saveBallsAfterOver();
            Player temp = striker;
            striker = nonStriker;
            nonStriker = temp;
            overCount++;
        }
        inningRepository.save(inning1);
        playerMatchPerformanceService.savePlayerStatsAfterMatch();
    }
    public int ballResult(){
        return (int) (Math.random() *10) % 8;
    }
}


