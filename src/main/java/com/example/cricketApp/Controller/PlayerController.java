package com.example.cricketApp.Controller;

import com.example.cricketApp.Entity.Player;
import com.example.cricketApp.Service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping
    public ResponseEntity<?> getAllPlayers(){
        return playerService.getAllPlayers();
    }

    @PostMapping
    public ResponseEntity<String> addPlayer(@RequestBody Player player) {
        return playerService.addPlayer(player);
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getPlayerById(@PathVariable int playerId) {
        return playerService.getPlayerById(playerId);
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<?> updatePlayerById(@RequestBody Player player, @PathVariable int playerId) {
        return playerService.updatePlayerById(player, playerId);
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<String> deletePlayerById(@PathVariable int playerId) {
        return playerService.deletePlayerById(playerId);
    }

    @PostMapping("/{playerId}/team/{teamId}")
    public ResponseEntity<String> addPlayerToTeam(@PathVariable int playerId, @PathVariable int teamId) {
        return playerService.addPlayerToTeam(playerId, teamId);
    }
}

