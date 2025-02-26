package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.Inning;
import com.example.cricketApp.Entity.Match;
import com.example.cricketApp.Entity.Over;
import com.example.cricketApp.Entity.Team;
import com.example.cricketApp.Repository.MatchRepository;
import com.example.cricketApp.Repository.OverRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OverService {
    @Autowired
    private OverRepository overRepository;

    public ObjectId createOver(int overNumber, int playerBowling, int matchId, ObjectId inningId){
        Over over= new Over(overNumber,playerBowling,matchId,inningId);
        overRepository.save(over);
        return overRepository.findAll().stream().filter((o) -> o.getOverNumber()==overNumber && o.getPlayerBowling()==playerBowling && o.getMatchId()==matchId && o.getInningId().equals(inningId)).findFirst().get().getOverId();
    }
}
