package com.example.cricketApp.Service;
import com.example.cricketApp.Entity.*;
import com.example.cricketApp.Repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BallService {
    @Autowired
    private BallRepository ballRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private OverService overService;
    @Autowired
    private PlayerMatchPerformanceService playerMatchPerformanceService;
    @Autowired
    private PlayerMatchPerformanceRepository playerMatchPerformanceRepository;

    private final List<Ball> ballsBuffer = new ArrayList<>();

    public boolean startBall(int ballOutcome, int i, Player striker, Player nonStriker, Player bowler, int wicketCount, int matchId, Inning inning1, Inning inning2, ObjectId overId){
        Match match= matchRepository.findById(matchId).get();
        int strikerId=striker.getPlayerId();
        int nonStrikerId=nonStriker.getPlayerId();
        int bowlerId = bowler.getPlayerId();
        ObjectId inningId1= inning1.getInningId();
        if(ballOutcome == 7){
            int ballNumber = i+1;
            int runsScored =0;
            int teamScore = inning1.getTotalRuns();
            boolean isWicket= true;
            inning1.setTotalWicketsFallen(inning1.getTotalWicketsFallen() + 1);
            bowler.setWicketsTaken(bowler.getWicketsTaken() + 1);
            striker.setBallsPlayed(striker.getBallsPlayed() + 1);
            addBallToBuffer(ballNumber,overId,matchId,inningId1,bowlerId,strikerId,nonStrikerId,runsScored,teamScore, inning1.getTotalWicketsFallen(), isWicket);

            PlayerMatchWiseStats bowlerStats = playerMatchPerformanceService.updatePlayerMatchPerformance(matchId,bowlerId,0, 0, 0,1,0,1,0);
            PlayerMatchWiseStats strikerStats = playerMatchPerformanceService.updatePlayerMatchPerformance(matchId,strikerId,0, 0,0, 0,1,0,0);
            PlayerMatchWiseStats nonStrikerStats= playerMatchPerformanceService.updatePlayerMatchPerformance(matchId,nonStrikerId,0,0 ,0,0,0,0,0);

            System.out.println("Wicket" + "\t" +  " Non-Striker: " + nonStriker.getPlayerName() + "--> " +nonStrikerStats.getRunsScored() + "(" + nonStrikerStats.getBallsFaced() + ")" + "\t" + "Bowler: " + bowler.getPlayerName() + "--> " + bowlerStats.getWicketsTaken() + "/" + bowlerStats.getRunsGiven() + "\t score: " + inning1.getTotalRuns() + "/" + inning1.getTotalWicketsFallen());
            System.out.println("Batsman out: " + striker.getPlayerName());

            List<Player> team1Players = playerRepository.findByTeamId(inning1.getTeamBattingId());
            int teamSize = team1Players.size();
            return inning1.getTotalWicketsFallen() == teamSize - 1;

        }
        else{
            striker.setBallsPlayed(striker.getBallsPlayed() +1);
            striker.setRunsScored(striker.getRunsScored()+ballOutcome);
            int ballNumber = i+1;
            int teamScore = inning1.getTotalRuns() + ballOutcome;
            boolean isWicket= false;
            inning1.setTotalRuns(teamScore);
            PlayerMatchWiseStats strikerStats;
            addBallToBuffer(ballNumber,overId,matchId,inningId1,bowlerId,strikerId,nonStrikerId,ballOutcome,teamScore,inning1.getTotalWicketsFallen(), isWicket);
            if(ballOutcome == 6){
                strikerStats = playerMatchPerformanceService.updatePlayerMatchPerformance(matchId,strikerId,ballOutcome, 1,0, 0,1,0,0);
            }
            else if(ballOutcome == 4){
                strikerStats = playerMatchPerformanceService.updatePlayerMatchPerformance(matchId,strikerId,ballOutcome, 0,1, 0,1,0,0);
            }
            else{
                strikerStats = playerMatchPerformanceService.updatePlayerMatchPerformance(matchId,strikerId,ballOutcome, 0,0, 0,1,0,0);
            }
            PlayerMatchWiseStats bowlerStats = playerMatchPerformanceService.updatePlayerMatchPerformance(matchId,bowlerId,0, 0, 0,0,0,1,ballOutcome);
            PlayerMatchWiseStats nonStrikerStats= playerMatchPerformanceService.updatePlayerMatchPerformance(matchId,nonStrikerId,0,0 ,0,0,0,0,0);

            System.out.println(ballOutcome + "\t" + "Striker: " + striker.getPlayerName() + "--> " + strikerStats.getRunsScored() + "(" + strikerStats.getBallsFaced() + ")" + "\t" + "Non-Striker: " + nonStriker.getPlayerName() + "--> " +nonStrikerStats.getRunsScored() + "(" + nonStrikerStats.getBallsFaced() + ")" + "\t" + "Bowler: " + bowler.getPlayerName() + "--> " + bowlerStats.getWicketsTaken() + "/" + bowlerStats.getRunsGiven() + "\t score: " + inning1.getTotalRuns() + "/" + inning1.getTotalWicketsFallen());
        }
        System.out.println("-------------------------------------------------------------------------------------------------------");

        if((inning2.getTotalRuns()!=0 || inning2.getTotalWicketsFallen()!=0 ) && (inning1.getTotalRuns() > inning2.getTotalRuns())){
            Team won = teamRepository.findById(inning1.getTeamBattingId()).orElseThrow(()-> new RuntimeException("No team found"));
            System.out.println("team won id : " + won.getTeamId());
            match.setTeamWonId(won.getTeamId());
            won.setNumberOfWins(won.getNumberOfWins() + 1);
            int wonBy= 10 - inning1.getTotalWicketsFallen();
            System.out.println("Team " + won.getTeamName() + " won by " + wonBy + " wickets 🏆" );
            MatchService.flag=true;
            matchRepository.save(match);
            teamRepository.save(won);
            return true;
        }
        System.out.println("-------------------------------------------------------------------------------------------------------");
        return false;
    }

    public void addBallToBuffer(int ballNumber, ObjectId overId, int matchId, ObjectId inningId, int bowlerId, int strikerId, int nonStrikerId, int runsScored, int teamScore, int wicketsFallen, boolean isWicket) {
        Ball ball = new Ball(ballNumber, overId, matchId, inningId, bowlerId, strikerId, nonStrikerId, runsScored, teamScore, wicketsFallen, isWicket);
        ballsBuffer.add(ball);
    }

    public void saveBallsAfterOver() {
        if (!ballsBuffer.isEmpty()) {
            ballRepository.saveAll(ballsBuffer);
            ballsBuffer.clear();
        }
    }

}

