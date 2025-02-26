package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.*;
import com.example.cricketApp.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private InningRepository inningRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayGameService playGameService;
    @Autowired
    private PlayerMatchPerformanceService playerMatchPerformanceService;
    @Autowired
    private ScorecardService scorecardService;

    public static boolean flag= false;

    public ResponseEntity<List<Match>> getAllMatches(){
        try {
            List <Match> matches = matchRepository.findAll();
            if (matches.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(matches, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> playMatch(int matchId) {
        try{
            Match match = matchRepository.findById(matchId).orElse(null);
            if(match==null){
                return new ResponseEntity<>("Match not found", HttpStatus.NOT_FOUND );
            }
            if(match.getTeamWonId()!=0){
                return new ResponseEntity<>("Match already played", HttpStatus.CONFLICT );
            }
            List <Inning> innings= inningRepository.findByMatchId(matchId);
            Inning inning1= innings.stream().filter((i) -> i.getInningNumber()==1).findFirst().get();
            Inning inning2= innings.stream().filter((i) -> i.getInningNumber()==2).findFirst().get();

            LinkedHashSet<Integer> set1 = new LinkedHashSet<>();
            LinkedHashSet<Integer> set2 = new LinkedHashSet<>();

            System.out.println("Inning 1 starts... ");
            playGameService.play(match,inning1,inning2,set1);
            Team batting = teamRepository.findById(inning1.getTeamBattingId()).orElseThrow(() -> new RuntimeException("No team found"));
            Team bowling = teamRepository.findById(inning1.getTeamBowlingId()).orElseThrow(() -> new RuntimeException("No team found"));
            scorecardService.printInningBattingScorecard(batting);
            scorecardService.printInningBowlingScorecard(bowling);
            System.out.println("Total score by " + batting.getTeamName() + ": " + inning1.getTotalRuns() + "/" + inning1.getTotalWicketsFallen());
            int target = inning1.getTotalRuns()+1;
            System.out.println("Target for team " + bowling.getTeamName() + " is --> " + target);
            System.out.println("-------------------------------------------------------------------------------------------------------");
            System.out.println();
            System.out.println("Inning 2 starts... ");
            playGameService.play(match,inning2,inning1,set2);
            scorecardService.printInningBattingScorecard(bowling);
            scorecardService.printInningBowlingScorecard(batting);
            playerMatchPerformanceService.updatePlayerFinalStats();

            if(inning1.getTotalRuns() > inning2.getTotalRuns() && !flag){
                Team won =  teamRepository.findById(inning1.getTeamBattingId()).orElseThrow(() -> new RuntimeException("No team found"));
                match.setTeamWonId(won.getTeamId());
                won.setNumberOfWins(won.getNumberOfWins() + 1);
                teamRepository.save(won);
                matchRepository.save(match);
                int wonBy= inning1.getTotalRuns() - inning2.getTotalRuns() + 1;
                System.out.println("Team "  + won.getTeamName() + " won by " + wonBy + " runs 🏆");
                return new ResponseEntity<>("Team "  + won.getTeamName() + " won by " + wonBy + " runs 🏆", HttpStatus.OK);
            }
            else if(inning1.getTotalRuns() < inning2.getTotalRuns() && !flag){
                Team won =  teamRepository.findById(inning2.getTeamBattingId()).orElseThrow(() -> new RuntimeException("No team found"));
                match.setTeamWonId(won.getTeamId());
                won.setNumberOfWins(won.getNumberOfWins() + 1);
                teamRepository.save(won);
                matchRepository.save(match);
                int wonBy= inning2.getTotalRuns() - inning1.getTotalRuns() + 1;
                System.out.println("Team "  + won.getTeamName() + " won by " + wonBy + " runs 🏆");
                return new ResponseEntity<>("Team "  + won.getTeamName() + " won by " + wonBy + " runs 🏆", HttpStatus.OK);
            }
            else if(!flag){
                match.setTeamWonId(-1);
                matchRepository.save(match);
                System.out.println("Match Tied");
                return new ResponseEntity<>("Match Tied" , HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error in playing match: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
