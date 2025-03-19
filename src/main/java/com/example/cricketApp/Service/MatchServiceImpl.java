package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.*;
import com.example.cricketApp.Kafka.Producer.MessageProducer;
import com.example.cricketApp.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.cricketApp.Service.ThreadPool.lock;
import static com.example.cricketApp.Service.ThreadPool.matchAvailable;

@Service
public class MatchServiceImpl implements MatchService {
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private InningRepository inningRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayGameServiceImpl playGameServiceImpl;
    @Autowired
    private PlayerMatchPerformanceServiceImpl playerMatchPerformanceServiceImpl;
    @Autowired
    private ScorecardServiceImpl scorecardServiceImpl;
    @Autowired
    private MessageProducer messageProducer;

    public static final ConcurrentHashMap<Integer, Boolean> matchFlags = new ConcurrentHashMap<>();

    public ResponseEntity<List<Match>> getAllMatches() {
        try {
            List<Match> matches = matchRepository.findAll();
            if (matches.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(matches, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> playMatch(int matchId) {
        try {
            Match match = initializeMatch(matchId);
            if (match == null) return new ResponseEntity<>("Match already played or not found", HttpStatus.CONFLICT);

            List<Inning> innings = inningRepository.findByMatchId(matchId);
            Inning inning1 = innings.stream().filter(i -> i.getInningNumber() == 1).findFirst().orElseThrow(() -> new RuntimeException("No inning 1 found"));
            Inning inning2 = innings.stream().filter(i -> i.getInningNumber() == 2).findFirst().orElseThrow(() -> new RuntimeException("No inning 2 found"));

            LinkedHashSet<Integer> set1 = new LinkedHashSet<>();
            LinkedHashSet<Integer> set2 = new LinkedHashSet<>();

            System.out.println(Thread.currentThread().getName() + " sleeping");
            Thread.sleep(5000);

            playInning(match, inning1, inning2, set1, true);
            playInning(match, inning2, inning1, set2, false);

            playerMatchPerformanceServiceImpl.updatePlayerFinalStats(matchId);
            String result = determineWinner(match, inning1, inning2);
            notifyWaitingTeams();

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<>("Error in playing match: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Match initializeMatch(int matchId) {
        Match match = matchRepository.findById(matchId).orElse(null);
        if (match == null || match.getTeamWonId() != 0) return null;
        return match;
    }

    private void playInning(Match match, Inning battingInning, Inning bowlingInning, LinkedHashSet<Integer> set, boolean isFirstInning) {
        System.out.println("Inning " + battingInning.getInningNumber() + " starts... ");
        playGameServiceImpl.play(match, battingInning, bowlingInning, set);

        Team batting = teamRepository.findById(battingInning.getTeamBattingId()).orElseThrow(() -> new RuntimeException("No team found"));
        Team bowling = teamRepository.findById(battingInning.getTeamBowlingId()).orElseThrow(() -> new RuntimeException("No team found"));

        scorecardServiceImpl.printInningBattingScorecard(match.getMatchId(), batting);
        scorecardServiceImpl.printInningBowlingScorecard(match.getMatchId(), bowling);

        System.out.println("Total score by " + batting.getTeamName() + ": " + battingInning.getTotalRuns() + "/" + battingInning.getTotalWicketsFallen());

        if (isFirstInning) {
            System.out.println("Target for team " + bowling.getTeamName() + " is --> " + (battingInning.getTotalRuns() + 1));
            System.out.println("-------------------------------------------------------------------------------------------------------");
        }
    }

    private String determineWinner(Match match, Inning inning1, Inning inning2) {
        if (matchFlags.get(match.getMatchId()) == null) {
            Team winningTeam;
            int wonBy;
            if (inning1.getTotalRuns() > inning2.getTotalRuns()) {
                winningTeam = teamRepository.findById(inning1.getTeamBattingId()).orElseThrow(() -> new RuntimeException("No team found"));
                wonBy = inning1.getTotalRuns() - inning2.getTotalRuns() + 1;
            } else if (inning1.getTotalRuns() < inning2.getTotalRuns()) {
                winningTeam = teamRepository.findById(inning2.getTeamBattingId()).orElseThrow(() -> new RuntimeException("No team found"));
                wonBy = inning2.getTotalRuns() - inning1.getTotalRuns() + 1;
            } else {
                match.setTeamWonId(-1);
                matchRepository.save(match);
                messageProducer.sendMatchResult("match-topic", "Match Tied!");
                return "Match Tied!";
            }
            match.setTeamWonId(winningTeam.getTeamId());
            winningTeam.setNumberOfWins(winningTeam.getNumberOfWins() + 1);
            teamRepository.save(winningTeam);
            matchRepository.save(match);

            String result = String.format("Team %s won by %d runs 🏆", winningTeam.getTeamName(), wonBy);
            System.out.println(result);
            messageProducer.sendMatchResult("match-topic", result);
            return result;
        }
        return "Match result already set!";
    }

    private void notifyWaitingTeams() {
        lock.lock();
        try {
            TossServiceImpl.teamsPlayingCurrently.clear();
            matchAvailable.signal();
            System.out.println("Match finished. Notifying all waiting threads.");
        } finally {
            lock.unlock();
            System.out.println("Lock released");
        }
    }
}

