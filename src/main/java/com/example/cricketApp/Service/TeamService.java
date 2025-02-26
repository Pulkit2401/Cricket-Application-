//package com.example.cricketApp.Service;
//
//import com.example.cricketApp.Entity.Player;
//import com.example.cricketApp.Entity.Team;
//import com.example.cricketApp.Repository.PlayerRepository;
//import com.example.cricketApp.Repository.TeamRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class TeamService {
//    @Autowired
//    private TeamRepository teamRepository;
//
//    @Autowired
//    private PlayerRepository playerRepository;
//
//    public List<Team> getAllTeams(){
//        return teamRepository.findAll();
//
//    }
//    public void addTeam(Team team) {
//        teamRepository.save(team);
//    }
//
//    public Team updateTeamById(Team team, int teamId){
//        Team oldTeam = teamRepository.findById(teamId).orElse(null);
//
//        if(oldTeam!=null){
//            oldTeam.setTeamId(team.getTeamId() != 0 ? team.getTeamId() : oldTeam.getTeamId());
//            oldTeam.setTeamName(team.getTeamName() != null && !team.getTeamName().equals("") ? team.getTeamName() : oldTeam.getTeamName());
//            teamRepository.save(oldTeam);
//        }
//        return oldTeam;
//
//    }
//    public Team getTeamById(int teamId) {
//        return teamRepository.findById(teamId).orElse(null);
//    }
//
//    public boolean deleteTeamById(int teamId) {
//        Team team= teamRepository.findById(teamId).orElse(null);
//        if(team==null) return false;
//        teamRepository.deleteById(teamId);
//        return true;
//    }
//}


package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.Player;
import com.example.cricketApp.Entity.Team;
import com.example.cricketApp.Repository.PlayerRepository;
import com.example.cricketApp.Repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;

    public ResponseEntity<List<Team>> getAllTeams() {
        try {
            List<Team> teams = teamRepository.findAll();
            if (teams.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(teams, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> addTeam(Team team) {
        try {
            teamRepository.save(team);
            return new ResponseEntity<>("Team added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding team: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Team> getTeamById(int teamId) {
        try {
            Optional<Team> team = teamRepository.findById(teamId);
            return team.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateTeamById(Team team, int teamId) {
        try {
            Optional<Team> teamOpt = teamRepository.findById(teamId);
            if (teamOpt.isEmpty()) {
                return new ResponseEntity<>("Team not found", HttpStatus.NOT_FOUND);
            }

            Team oldTeam = teamOpt.get();
            oldTeam.setTeamName(!team.getTeamName().isEmpty() ? team.getTeamName() : oldTeam.getTeamName());
            teamRepository.save(oldTeam);

            return new ResponseEntity<>(oldTeam, HttpStatus.OK);
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

