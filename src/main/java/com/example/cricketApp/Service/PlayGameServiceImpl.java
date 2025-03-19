package com.example.cricketApp.Service;//package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.*;
import com.example.cricketApp.Kafka.Producer.MessageProducer;
import com.example.cricketApp.Repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;


@Service
public class PlayGameServiceImpl implements PlayGameService {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private InningRepository inningRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private OverServiceImpl overServiceImpl;
    @Autowired
    private BallServiceImpl ballServiceImpl;
    @Autowired
    private PlayerMatchPerformanceServiceImpl playerMatchPerformanceServiceImpl;
    @Autowired
    private PlayerMatchPerformanceRepository playerMatchPerformanceRepository;
    @Autowired
    private MessageProducer messageProducer;

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
            ObjectId overId = overServiceImpl.createOver(overCount, bowlerId, matchId, inningId1);

            for (int i = 0; i < 6; i++) {
                int ballOutcome = ballResult();
                boolean ball = ballServiceImpl.startBall(ballOutcome, i, striker, nonStriker, bowler, wicketCount, matchId, inning1, inning2, overId);
                if (ball) {
                    if ((inning2.getTotalRuns() != 0 || inning2.getTotalWicketsFallen() != 0) && inning1.getTotalWicketsFallen() == team1Players.size() - 1) {
                        Team won = teamRepository.findById(inning2.getTeamBattingId()).orElseThrow(() -> new RuntimeException("No team found"));
                        System.out.println("team won id : " + won.getTeamId());
                        match.setTeamWonId(won.getTeamId());
                        won.setNumberOfWins(won.getNumberOfWins() + 1);
                        int wonBy = inning2.getTotalRuns() - inning1.getTotalRuns() + 1;
                        String result = "Team " + won.getTeamName() + " won by " + wonBy + " runs 🏆";
                        System.out.println("Team " + won.getTeamName() + " won by " + wonBy + " runs 🏆");
                        messageProducer.sendMatchResult("match-topic", result);
                        MatchServiceImpl.matchFlags.put(matchId, true);
                        matchRepository.save(match);
                        teamRepository.save(won);
                    }
                    ballServiceImpl.saveBallsAfterOver();
                    inningRepository.save(inning1);
                    playerMatchPerformanceServiceImpl.savePlayerStatsAfterMatch(matchId);
                    return;
                }
                if (ballOutcome == 7) {
                    for (Player p : team1Players) {
                        if (!setPlayers.contains(p.getPlayerId())) {
                            setPlayers.add(p.getPlayerId());
                            striker = p;
                            System.out.println("Next batsman coming: " + striker.getPlayerName());
                            break;
                        }
                    }
                }
                if (ballOutcome % 2 == 1 && ballOutcome != 7) {
                    Player temp = striker;
                    striker = nonStriker;
                    nonStriker = temp;
                }
            }
            ballServiceImpl.saveBallsAfterOver();
            Player temp = striker;
            striker = nonStriker;
            nonStriker = temp;
            overCount++;
        }
        inningRepository.save(inning1);
        playerMatchPerformanceServiceImpl.savePlayerStatsAfterMatch(matchId);
    }

    public int ballResult() {
        return (int) (Math.random() * 10) % 8;
    }
}


