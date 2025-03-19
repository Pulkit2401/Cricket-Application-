package com.example.cricketApp.Service;

import com.example.cricketApp.Dto.TeamDto;
import com.example.cricketApp.Entity.Team;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TeamService {

    ResponseEntity<List<Team>> getAllTeams();

    ResponseEntity<String> addTeam(TeamDto teamDto);

    ResponseEntity<Team> getTeamById(int teamId);

    ResponseEntity<?> updateTeamById(TeamDto teamDto, int teamId);

    ResponseEntity<String> deleteTeamById(int teamId);
}
