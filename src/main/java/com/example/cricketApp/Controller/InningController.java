package com.example.cricketApp.Controller;

import com.example.cricketApp.Entity.Inning;
import com.example.cricketApp.Service.InningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inning")
public class InningController {
    @Autowired
    private InningService inningService;

    @GetMapping
    public ResponseEntity<List<Inning>> getAllInnings(){
        return inningService.getAllInnings();
    }

}
