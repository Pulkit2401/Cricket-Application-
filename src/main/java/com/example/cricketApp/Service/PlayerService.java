package com.example.cricketApp.Service;

import com.example.cricketApp.Dto.PlayerRequestDto;
import org.springframework.http.ResponseEntity;

public interface PlayerService {

    ResponseEntity<?> getAllPlayers();

    ResponseEntity<String> addPlayer(PlayerRequestDto playerRequestDto);

    ResponseEntity<?> getPlayerById(int playerId);

    ResponseEntity<?> updatePlayerById(PlayerRequestDto playerRequestDto, int playerId);

    ResponseEntity<String> deletePlayerById(int playerId);

    ResponseEntity<String> addPlayerToTeam(int playerId, int teamId);
}
