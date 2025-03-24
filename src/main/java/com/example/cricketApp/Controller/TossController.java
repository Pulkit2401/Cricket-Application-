package com.example.cricketApp.Controller;

import com.example.cricketApp.Dto.MatchRequestDto;
import com.example.cricketApp.Entity.Team;
import com.example.cricketApp.Repository.TeamRepository;
import com.example.cricketApp.Service.TossServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/toss")
public class TossController {

    @Autowired
    private TossServiceImpl tossServiceImpl;
    @Autowired
    private TeamRepository teamRepository;

    @PostMapping("/team/{teamId1}/{teamId2}")
    public ResponseEntity<?> performToss(@RequestBody MatchRequestDto matchRequestDto, @PathVariable int teamId1, @PathVariable int teamId2) throws ExecutionException, InterruptedException {
        Team team1= teamRepository.findById(teamId1).get();
        Team team2= teamRepository.findById(teamId2).get();
        return tossServiceImpl.toss(matchRequestDto,team1,team2);
    }

}
