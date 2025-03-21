package com.example.cricketApp.Controller;

import com.example.cricketApp.Dto.PlayerDto;
import com.example.cricketApp.Entity.Player;
import com.example.cricketApp.Service.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerServiceImpl playerServiceImpl;

    @GetMapping
    public ResponseEntity<?> getAllPlayers(){
        return playerServiceImpl.getAllPlayers();
    }

    @PostMapping
    public ResponseEntity<String> addPlayer(@RequestBody PlayerDto playerDto) {
        return playerServiceImpl.addPlayer(playerDto);
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getPlayerById(@PathVariable int playerId) {
        return playerServiceImpl.getPlayerById(playerId);
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<?> updatePlayerById(@RequestBody PlayerDto playerDto, @PathVariable int playerId) {
        return playerServiceImpl.updatePlayerById(playerDto, playerId);
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

