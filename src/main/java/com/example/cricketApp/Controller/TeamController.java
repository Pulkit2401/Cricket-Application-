//package com.example.cricketApp.Controller;
//
//import com.example.cricketApp.Entity.Team;
//import com.example.cricketApp.Service.TeamService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/team")
//public class TeamController {
//    @Autowired
//    private TeamService teamService;
//
//    @GetMapping
//    public List<Team> getAllTeams(){
//        return teamService.getAllTeams();
//    }
//
//    @PostMapping
//    public void addTeam(@RequestBody Team team){
//        teamService.addTeam(team);
//    }
//
//    @GetMapping("/{teamId}")
//    public Team getTeamById(@PathVariable int teamId){
//        return teamService.getTeamById(teamId);
//    }
//
//    @PutMapping("/{teamId}")
//    public Team updateTeamById(@RequestBody Team team, @PathVariable int teamId){
//       return teamService.updateTeamById(team, teamId);
//    }
//
//    @DeleteMapping("/{teamId}")
//    public boolean deleteTeamById(@PathVariable int teamId){
//       return teamService.deleteTeamById(teamId);
//    }
//
//}



package com.example.cricketApp.Controller;

import com.example.cricketApp.Entity.Team;
import com.example.cricketApp.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return teamService.getAllTeams();
    }

    @PostMapping
    public ResponseEntity<String> addTeam(@RequestBody Team team) {
        return teamService.addTeam(team);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeamById(@PathVariable int teamId) {
        return teamService.getTeamById(teamId);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<?> updateTeamById(@RequestBody Team team, @PathVariable int teamId) {
        return teamService.updateTeamById(team, teamId);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeamById(@PathVariable int teamId) {
        return teamService.deleteTeamById(teamId);
    }
}

