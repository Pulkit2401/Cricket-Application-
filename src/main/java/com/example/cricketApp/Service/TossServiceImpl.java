package com.example.cricketApp.Service;
import com.example.cricketApp.Dto.MatchDto;
import com.example.cricketApp.Entity.Inning;
import com.example.cricketApp.Entity.Match;
import com.example.cricketApp.Entity.Player;
import com.example.cricketApp.Entity.Team;
import com.example.cricketApp.Repository.MatchRepository;
import com.example.cricketApp.Repository.PlayerRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.example.cricketApp.Service.ThreadPool.lock;
import static com.example.cricketApp.Service.ThreadPool.matchAvailable;


@Service
public class TossServiceImpl implements TossService {
    public static final Set<Integer> teamsPlayingCurrently = ConcurrentHashMap.newKeySet();
    public static final Set<Integer> teamsWaiting = ConcurrentHashMap.newKeySet();
    @Autowired
    private TeamServiceImpl teamServiceImpl;
    @Autowired
    private InningServiceImpl inningServiceImpl;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private ObjectProvider<Inning> inningProvider;
    @Autowired
    private PlayerRepository playerRepository;

    @Transactional
    public ResponseEntity<?> performToss(MatchDto matchDto, int teamId1, int teamId2) {
        Team team1;
        Team team2;
        System.out.println("Toss time....");
        int toss = (int) (Math.random() * 10) % 2;

        Match match = Match.builder()
                .matchId(matchDto.getMatchId())
                .venue(matchDto.getVenue())
                .overs(matchDto.getOvers())
                .build();

        if (toss == 0) {
            team1 = teamServiceImpl.getTeamById(teamId1).getBody();
            team2 = teamServiceImpl.getTeamById(teamId2).getBody();
            if (team1 == null) {
                return new ResponseEntity<>("Team not found", HttpStatus.NOT_FOUND);
            }
            if (team2 == null) {
                return new ResponseEntity<>("Team not found", HttpStatus.NOT_FOUND);
            }
            System.out.println("Team " + team1.getTeamName() + " won the toss. They will bat first");
        } else {
            team1 = teamServiceImpl.getTeamById(teamId2).getBody();
            team2 = teamServiceImpl.getTeamById(teamId1).getBody();
            if (team1 == null) {
                return new ResponseEntity<>("Team not found", HttpStatus.NOT_FOUND);
            }
            if (team2 == null) {
                return new ResponseEntity<>("Team not found", HttpStatus.NOT_FOUND);
            }
            System.out.println("Team " + team1.getTeamName() + " won the toss. They will bat first");
        }

        int tId1 = team1.getTeamId();
        int tId2 = team2.getTeamId();

        Inning inning1 = inningProvider.getObject();
        Inning inning2 = inningProvider.getObject();

        int matchId = match.getMatchId();
        int overs = match.getOvers();
        if (matchId == 0) {
            return new ResponseEntity<>("Match Id not set", HttpStatus.BAD_REQUEST);
        }
        if (overs == 0) {
            return new ResponseEntity<>("Overs can not be set to 0", HttpStatus.BAD_REQUEST);
        }
        inning1.setInningNumber(1);
        inning2.setInningNumber(2);
        inningServiceImpl.createInning(matchId, inning1, overs, tId1, tId2);
        inningServiceImpl.createInning(matchId, inning2, overs, tId2, tId1);
        List<Player> team1Players = playerRepository.findByTeamId(tId1);
        List<Player> team2Players = playerRepository.findByTeamId(tId2);
        team1Players.forEach(p -> {
            p.setMatchesPlayed(p.getMatchesPlayed() + 1);
            playerRepository.save(p);
        });
        team2Players.forEach(p -> {
            p.setMatchesPlayed(p.getMatchesPlayed() + 1);
            playerRepository.save(p);
        });
        match.setTeamAId(tId1);
        match.setTeamBId(tId2);
        match.setMatchDate(LocalDate.now());
        matchRepository.save(match);
        System.out.println("Match Details : ");
        System.out.println("Total Overs: " + match.getOvers());
        System.out.println("Venue: " + match.getVenue());
        System.out.println();

        return new ResponseEntity<>(team1.getTeamName() + " won the toss and will bat first", HttpStatus.OK);
    }

    public ResponseEntity<?> toss(MatchDto matchDto, Team team1, Team team2) throws ExecutionException, InterruptedException {
        if (matchRepository.existsById(matchDto.getMatchId())) {
            return new ResponseEntity<>("This match already exists", HttpStatus.CONFLICT);
        }
        Future<ResponseEntity<?>> futureObj = ThreadPool.matchExecutor.submit(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " acquired lock and checking if teams are available.");

                while (teamsPlayingCurrently.contains(team1.getTeamId()) || teamsPlayingCurrently.contains(team2.getTeamId())) {
                    teamsWaiting.add(team1.getTeamId());
                    teamsWaiting.add(team2.getTeamId());
                    System.out.println(Thread.currentThread().getName() + " waiting as teams are already playing.");
                    matchAvailable.await();
                    System.out.println(Thread.currentThread().getName() + " woke up and checking again.");
                }

                System.out.println(Thread.currentThread().getName() + " proceeding with toss.");
                teamsPlayingCurrently.add(team1.getTeamId());
                teamsPlayingCurrently.add(team2.getTeamId());
                teamsWaiting.remove(team1.getTeamId());
                teamsWaiting.remove(team2.getTeamId());

                return performToss(matchDto, team1.getTeamId(), team2.getTeamId());
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + " was interrupted.");
                return new ResponseEntity<>("Error in scheduling match", HttpStatus.INTERNAL_SERVER_ERROR);
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " released lock.");
            }
        });

        return futureObj.get();
    }

}


