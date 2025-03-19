package com.example.cricketApp.Controller;

import com.example.cricketApp.Dto.TeamDto;
import com.example.cricketApp.Entity.Team;
import com.example.cricketApp.Service.TeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private TeamServiceImpl teamServiceImpl;

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return teamServiceImpl.getAllTeams();
    }

    @PostMapping
    public ResponseEntity<String> addTeam(@RequestBody TeamDto teamDto) {
        return teamServiceImpl.addTeam(teamDto);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeamById(@PathVariable int teamId) {
        return teamServiceImpl.getTeamById(teamId);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<?> updateTeamById(@RequestBody TeamDto teamDto, @PathVariable int teamId) {
        return teamServiceImpl.updateTeamById(teamDto, teamId);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeamById(@PathVariable int teamId) {
        return teamServiceImpl.deleteTeamById(teamId);
    }
}

