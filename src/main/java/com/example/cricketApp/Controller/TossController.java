package com.example.cricketApp.Controller;

import com.example.cricketApp.Entity.Match;
import com.example.cricketApp.Entity.Team;
import com.example.cricketApp.Service.MatchService;
import com.example.cricketApp.Service.TossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/toss")
public class TossController {

    @Autowired
    private TossService tossService;

    @PostMapping("/team/{teamId1}/{teamId2}")
    public ResponseEntity<?> performToss(@RequestBody Match match, @PathVariable int teamId1, @PathVariable int teamId2){
        return tossService.performToss(match,teamId1,teamId2);
    }

}
