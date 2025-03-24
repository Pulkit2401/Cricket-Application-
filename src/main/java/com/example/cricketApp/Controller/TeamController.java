package com.example.cricketApp.Controller;

import com.example.cricketApp.Dto.TeamRequestDto;
import com.example.cricketApp.Dto.TeamResponseDto;
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
    public ResponseEntity<List<TeamResponseDto>> getAllTeams() {
        return teamServiceImpl.getAllTeams();
    }

    @PostMapping
    public ResponseEntity<String> addTeam(@RequestBody TeamRequestDto teamRequestDto) {
        return teamServiceImpl.addTeam(teamRequestDto);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> getTeamById(@PathVariable int teamId) {
        return teamServiceImpl.getTeamById(teamId);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<?> updateTeamById(@RequestBody TeamRequestDto teamRequestDto, @PathVariable int teamId) {
        return teamServiceImpl.updateTeamById(teamRequestDto, teamId);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeamById(@PathVariable int teamId) {
        return teamServiceImpl.deleteTeamById(teamId);
    }
}

