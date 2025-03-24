package com.example.cricketApp.Controller;

import com.example.cricketApp.Dto.PlayerRequestDto;
import com.example.cricketApp.Dto.PlayerResponseDto;
import com.example.cricketApp.Service.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerServiceImpl playerServiceImpl;

    @GetMapping
    public ResponseEntity<List<PlayerResponseDto>> getAllPlayers(){
        return playerServiceImpl.getAllPlayers();
    }

    @PostMapping
    public ResponseEntity<String> addPlayer(@RequestBody PlayerRequestDto playerRequestDto) {
        return playerServiceImpl.addPlayer(playerRequestDto);
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getPlayerById(@PathVariable int playerId) {
        return playerServiceImpl.getPlayerById(playerId);
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<?> updatePlayerById(@RequestBody PlayerRequestDto playerRequestDto, @PathVariable int playerId) {
        return playerServiceImpl.updatePlayerById(playerRequestDto, playerId);
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<String> deletePlayerById(@PathVariable int playerId) {
        return playerServiceImpl.deletePlayerById(playerId);
    }

    @PostMapping("/{playerId}/team/{teamId}")
    public ResponseEntity<String> addPlayerToTeam(@PathVariable int playerId, @PathVariable int teamId) {
        return playerServiceImpl.addPlayerToTeam(playerId, teamId);
    }
}

