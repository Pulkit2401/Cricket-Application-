package com.example.cricketApp.Service;

import com.example.cricketApp.Dto.TeamRequestDto;
import com.example.cricketApp.Dto.TeamResponseDto;
import com.example.cricketApp.Entity.Team;
import com.example.cricketApp.Mapper.TeamMapper;
import com.example.cricketApp.Repository.PlayerRepository;
import com.example.cricketApp.Repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;

    public ResponseEntity<List<TeamResponseDto>> getAllTeams() {
        try {
            List<Team> teams = teamRepository.findAll();
            if (teams.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<TeamResponseDto> teamResponseDtos = teams.stream().map(TeamMapper::toDto).toList();
            return new ResponseEntity<>(teamResponseDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> addTeam(TeamRequestDto teamRequestDto) {
        try {
            Team team = Team.builder()
                            .teamId(teamRequestDto.getTeamId())
                                    .teamName(teamRequestDto.getTeamName())
                                            .build();
            teamRepository.save(team);
            return new ResponseEntity<>("Team added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding team: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<TeamResponseDto> getTeamById(int teamId) {
        try {
            Optional <Team> teamObj = teamRepository.findById(teamId);
            if(teamObj.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            TeamResponseDto teamResponseDto = TeamMapper.toDto(teamObj.get());
            return  new ResponseEntity<>(teamResponseDto, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateTeamById(TeamRequestDto teamRequestDto, int teamId) {
        try {
            Optional<Team> teamOpt = teamRepository.findById(teamId);
            if (teamOpt.isEmpty()) {
                return new ResponseEntity<>("Team not found", HttpStatus.NOT_FOUND);
            }

            Team oldTeam = teamOpt.get();
            oldTeam.setTeamName(!teamRequestDto.getTeamName().isEmpty() ? teamRequestDto.getTeamName() : oldTeam.getTeamName());
            teamRepository.save(oldTeam);
            TeamResponseDto teamResponseDto = TeamMapper.toDto(oldTeam);

            return new ResponseEntity<>(teamResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating team: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteTeamById(int teamId) {
        try {
            Optional<Team> teamOpt = teamRepository.findById(teamId);
            if (teamOpt.isEmpty()) {
                return new ResponseEntity<>("Team not found", HttpStatus.NOT_FOUND);
            }
            playerRepository.findAll().forEach(p-> {if(p.getTeamId()==teamId){ p.setTeamId(0);
            playerRepository.save(p);}});
            teamRepository.deleteById(teamId);
            return new ResponseEntity<>("Team deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting team: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

