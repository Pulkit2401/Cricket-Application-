package com.example.cricketApp.Service;

import com.example.cricketApp.Dto.PlayerRequestDto;
import com.example.cricketApp.Dto.PlayerResponseDto;
import com.example.cricketApp.Entity.Player;
import com.example.cricketApp.Entity.Team;
import com.example.cricketApp.Mapper.PlayerMapper;
import com.example.cricketApp.Repository.PlayerRepository;
import com.example.cricketApp.Repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RedisServiceImpl redisServiceImpl;

    public ResponseEntity<List<PlayerResponseDto>> getAllPlayers() {
        try {
            List<Player> players = playerRepository.findAll();
            List<PlayerResponseDto> playerResponseDtos = players.stream().map(PlayerMapper::toDto).toList();
            return new ResponseEntity<>(playerResponseDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> addPlayer(PlayerRequestDto playerRequestDto) {
        try {
            if (playerRepository.existsById(playerRequestDto.getPlayerId())) {
                return new ResponseEntity<>("Player with this ID already exists", HttpStatus.CONFLICT);
            }
            Player player = Player.builder()
                            .playerId(playerRequestDto.getPlayerId())
                                    .playerName(playerRequestDto.getPlayerName())
                                            .playerType(playerRequestDto.getPlayerType())
                                                    .build();

            playerRepository.save(player);
            return new ResponseEntity<>("Player added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding player", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getPlayerById(int playerId) {
        try {
            String key = "Stats_of_player_with_id" + (playerId);
            Player cachedPlayer = (Player) redisServiceImpl.getPlayerStats(key);
            if (cachedPlayer != null) {
                return new ResponseEntity<>(cachedPlayer, HttpStatus.OK);
            }

            Player player = playerRepository.findById(playerId).orElse(null);
            if (player == null) {
                return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
            }
            PlayerResponseDto playerResponseDto = PlayerMapper.toDto(player);
            redisServiceImpl.savePlayerStats(key, player, 1, TimeUnit.DAYS);
            return new ResponseEntity<>(playerResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving player", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updatePlayerById(PlayerRequestDto playerRequestDto, int playerId) {
        try {
            Player oldPlayer = playerRepository.findById(playerId).orElse(null);
            if (oldPlayer != null) {
                oldPlayer.setPlayerId(playerRequestDto.getPlayerId() != 0 ? playerRequestDto.getPlayerId() : oldPlayer.getPlayerId());
                oldPlayer.setPlayerName(!playerRequestDto.getPlayerName().equals("") ? playerRequestDto.getPlayerName() : oldPlayer.getPlayerName());
                oldPlayer.setPlayerType(playerRequestDto.getPlayerType() != null ? playerRequestDto.getPlayerType() : oldPlayer.getPlayerType());
                playerRepository.save(oldPlayer);

                PlayerResponseDto playerResponseDto = PlayerMapper.toDto(oldPlayer);
                return new ResponseEntity<>(playerResponseDto, HttpStatus.OK);
            }
            return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating player", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deletePlayerById(int playerId) {
        try {
            String key = "Stats_of_player_with_id" + (playerId);
            Player cachedPlayer = (Player) redisServiceImpl.getPlayerStats(key);
            Player player = playerRepository.findById(playerId).orElse(null);
            if (player == null) {
                return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
            }
            if (cachedPlayer != null) {
                redisServiceImpl.deletePlayerStats(key);
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
