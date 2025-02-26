package com.example.cricketApp.Controller;

import com.example.cricketApp.Entity.Ball;
import com.example.cricketApp.Entity.Inning;
import com.example.cricketApp.Entity.Match;
import com.example.cricketApp.Service.InningService;
import com.example.cricketApp.Service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping
    public ResponseEntity<List<Match>>getAllMatches(){
        return matchService.getAllMatches();
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<?> playMatch(@PathVariable int matchId){
        return matchService.playMatch(matchId);
    }
}
