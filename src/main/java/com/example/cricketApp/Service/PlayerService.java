package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.Player;
import com.example.cricketApp.Entity.Team;
import com.example.cricketApp.Repository.PlayerRepository;
import com.example.cricketApp.Repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    public ResponseEntity<List<Player>> getAllPlayers() {
        try {
            List<Player> players = playerRepository.findAll();
            return new ResponseEntity<>(players, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> addPlayer(Player player) {
        try {
            if (playerRepository.existsById(player.getPlayerId())) {
                return new ResponseEntity<>("Player with this ID already exists", HttpStatus.CONFLICT);
            }
            playerRepository.save(player);
            return new ResponseEntity<>("Player added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding player", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getPlayerById(int playerId) {
        try {
            Player player = playerRepository.findById(playerId).orElse(null);
            if (player == null) {
                return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(player, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving player", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updatePlayerById(Player player, int playerId) {
        try {
            Player oldPlayer = playerRepository.findById(playerId).orElse(null);
            if (oldPlayer != null) {
                oldPlayer.setPlayerId(player.getPlayerId() != 0 ? player.getPlayerId() : oldPlayer.getPlayerId());
                oldPlayer.setPlayerName(!player.getPlayerName().equals("") ? player.getPlayerName() : oldPlayer.getPlayerName());
                oldPlayer.setRunsScored(player.getRunsScored() != 0 ? player.getRunsScored() : oldPlayer.getRunsScored());
                oldPlayer.setWicketsTaken(player.getWicketsTaken() != 0 ? player.getWicketsTaken() : oldPlayer.getWicketsTaken());
                oldPlayer.setBallsPlayed(player.getBallsPlayed() != 0 ? player.getBallsPlayed() : oldPlayer.getBallsPlayed());
                oldPlayer.setPlayerType(player.getPlayerType() != null ? player.getPlayerType() : oldPlayer.getPlayerType());
                oldPlayer.setTeamId(player.getTeamId() != 0 ? player.getTeamId() : oldPlayer.getTeamId());
                oldPlayer.setMatchesPlayed(player.getMatchesPlayed() != 0 ? player.getMatchesPlayed() : oldPlayer.getMatchesPlayed());
                oldPlayer.setFifties(player.getFifties() != 0 ? player.getFifties() : oldPlayer.getFifties());
                oldPlayer.setHundreds(player.getHundreds() != 0 ? player.getHundreds() : oldPlayer.getHundreds());
                oldPlayer.setFiveWicketHauls(player.getFiveWicketHauls() != 0 ? player.getFiveWicketHauls() : oldPlayer.getFiveWicketHauls());
                playerRepository.save(oldPlayer);
                return new ResponseEntity<>(oldPlayer, HttpStatus.OK);
            }
            return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating player", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deletePlayerById(int playerId) {
        try {
            Player player = playerRepository.findById(playerId).orElse(null);
            if (player == null) {
                return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
            }
            playerRepository.deleteById(playerId);
            return new ResponseEntity<>("Player deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting player", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<String> addPlayerToTeam(int playerId, int teamId) {
        try {
            Optional<Team> teamOpt = teamRepository.findById(teamId);
            Optional<Player> playerOpt = playerRepository.findById(playerId);

            if (teamOpt.isEmpty()) {
                return new ResponseEntity<>("Team not found", HttpStatus.NOT_FOUND);
            }

            if (playerOpt.isEmpty()) {
                return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
            }

            Player player = playerOpt.get();

            if (player.getTeamId() == teamId) {
                return new ResponseEntity<>("Player is already in this team", HttpStatus.CONFLICT);
            }

            player.setTeamId(teamId);
            playerRepository.save(player);

            return new ResponseEntity<>("Player added to team successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error adding player to team: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
