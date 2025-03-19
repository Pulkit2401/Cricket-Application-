package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.*;
import com.example.cricketApp.Repository.InningRepository;
import com.example.cricketApp.Repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InningServiceImpl implements InningService {
    @Autowired
    private InningRepository inningRepository;
    @Autowired
    private TeamRepository teamRepository;

    public ResponseEntity<List<Inning>> getAllInnings(){
        try {
            List <Inning> innings = inningRepository.findAll();
            if (innings.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(innings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    public void createInning(int matchId, Inning inning, int overs, int tId1, int tId2){
        inning.setMatchId(matchId);
        inning.setTotalOvers(overs);
        inning.setTeamBattingId(tId1);
        inning.setTeamBowlingId(tId2);
        inning.setTotalOvers(overs);
        inningRepository.save(inning);
    }

}
