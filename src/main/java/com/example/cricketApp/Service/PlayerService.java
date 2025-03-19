package com.example.cricketApp.Service;

import com.example.cricketApp.Dto.PlayerDto;
import com.example.cricketApp.Entity.Player;
import org.springframework.http.ResponseEntity;

public interface PlayerService {

    ResponseEntity<?> getAllPlayers();

    ResponseEntity<String> addPlayer(PlayerDto playerDto);

    ResponseEntity<?> getPlayerById(int playerId);

    ResponseEntity<?> updatePlayerById(PlayerDto playerDto, int playerId);

    ResponseEntity<String> deletePlayerById(int playerId);

    ResponseEntity<String> addPlayerToTeam(int playerId, int teamId);
}
