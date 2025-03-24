package com.example.cricketApp.Controller;

import com.example.cricketApp.Dto.MatchResponseDto;
import com.example.cricketApp.Entity.Match;
import com.example.cricketApp.Service.MatchServiceImpl;
import com.example.cricketApp.Service.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private MatchServiceImpl matchServiceImpl;

    @GetMapping
    public ResponseEntity<List<MatchResponseDto>>getAllMatches(){
        return matchServiceImpl.getAllMatches();
    }

    @GetMapping("/play/{matchId}")
    public ResponseEntity<?> playMatch(@PathVariable int matchId) {
        try {
            Future<ResponseEntity<?>> futureResult = ThreadPool.matchExecutor.submit(() -> matchServiceImpl.playMatch(matchId));
            return futureResult.get();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error executing match: " + e.getMessage());
        }
    }
}
