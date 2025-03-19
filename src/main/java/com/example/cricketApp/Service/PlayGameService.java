package com.example.cricketApp.Service;

import com.example.cricketApp.Entity.Inning;
import com.example.cricketApp.Entity.Match;
import java.util.LinkedHashSet;

public interface PlayGameService {

    void play(Match match, Inning inning1, Inning inning2, LinkedHashSet<Integer> setPlayers);

    int ballResult();
}