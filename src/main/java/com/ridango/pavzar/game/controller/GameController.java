package com.ridango.pavzar.game.controller;

import com.ridango.pavzar.game.model.GameState;
import com.ridango.pavzar.game.model.GuessRequest;
import com.ridango.pavzar.game.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/start")
    public GameState startGame() {
        return gameService.startGame();
    }

    @PostMapping("/guess")
    public GameState makeGuess(@RequestBody GuessRequest guessRequest) {
        return gameService.makeGuess(guessRequest);
    }

    @GetMapping("/skip")
    public GameState skipRound() {
        return gameService.skipRound();
    }
}