package com.example.cricketApp.Service;

import com.example.cricketApp.Dto.TeamRequestDto;
import com.example.cricketApp.Dto.TeamResponseDto;
import com.example.cricketApp.Entity.Team;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TeamService {

    ResponseEntity<List<TeamResponseDto>> getAllTeams();

    ResponseEntity<String> addTeam(TeamRequestDto teamRequestDto);

    ResponseEntity<TeamResponseDto> getTeamById(int teamId);

    ResponseEntity<?> updateTeamById(TeamRequestDto teamRequestDto, int teamId);

    ResponseEntity<String> deleteTeamById(int teamId);
}
