package com.ridango.pavzar.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameState {
    private String cocktailName;
    private String hiddenCocktailName;
    private int attemptsLeft;
    private String instructions;
    private List<String> revealedHints;
    private String message;
    private int highScore;
    private int currentScore;
}

