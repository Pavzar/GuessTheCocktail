package com.ridango.pavzar.game.service;

import com.ridango.pavzar.game.entity.Cocktail;
import com.ridango.pavzar.game.model.GameState;
import com.ridango.pavzar.game.model.GuessRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @MockBean
    private CocktailService cocktailService;

    @MockBean
    private HighScoreService highScoreService;

    @BeforeEach
    public void setup() {
        Cocktail mockCocktail1 = new Cocktail(null, 1L, "Margarita", "Ordinary Drink", "Instructions...",
                "Cocktail glass", "image_url", "Tequila", "Lime juice", "Cointreau", null,
                "2 oz", "1 oz", "1/2 oz", null);
        Cocktail mockCocktail2 = new Cocktail(null, 2L, "Daiquiri", "Ordinary Drink", "Instructions for Daiquiri...",
                "Cocktail glass", "image_url2", "Rum", "Lime juice", "Sugar", null,
                "2 oz", "1 oz", "1/2 oz", null);

        when(cocktailService.getRandomCocktail()).thenReturn(mockCocktail1).thenReturn(mockCocktail2);
        when(highScoreService.getHighScore()).thenReturn(10);
    }

    @Test
    public void testStartGame() {
        GameState gameState = gameService.startGame();

        assertEquals("Margarita", gameState.getCocktailName());
        assertEquals("_________", gameState.getHiddenCocktailName());
        assertEquals(5, gameState.getAttemptsLeft());
        assertEquals("Instructions...", gameState.getInstructions());
        assertEquals("Good Luck.", gameState.getMessage());
        assertEquals(10, gameState.getHighScore());
        assertEquals(0, gameState.getCurrentScore());
    }

    @Test
    public void testMakeGuess_Correct() {
        GameState initialGameState = gameService.startGame(); // Start the game once

        String initialCocktailName = initialGameState.getCocktailName();
        int initialScore = initialGameState.getCurrentScore();
        List<String> initialRevealedHints = initialGameState.getRevealedHints();
        String initialInstructions = initialGameState.getInstructions();

        GuessRequest guessRequest = new GuessRequest();
        guessRequest.setGuess(initialCocktailName);

        GameState gameState = gameService.makeGuess(guessRequest);

        assertNotEquals(initialCocktailName, gameState.getCocktailName());
        assertTrue(gameState.getHiddenCocktailName().contains("_"));
        assertEquals(5, gameState.getAttemptsLeft());
        assertEquals("Correct guess.", gameState.getMessage());
        assertTrue(gameState.getCurrentScore() > initialScore);
        assertEquals(initialRevealedHints, gameState.getRevealedHints());
        assertNotEquals(initialInstructions, gameState.getInstructions());
    }

    @Test
    public void testMakeGuess_Incorrect() {
        gameService.startGame();

        GuessRequest guessRequest = new GuessRequest();
        guessRequest.setGuess("Daiquiri");

        GameState gameState = gameService.makeGuess(guessRequest);
        assertEquals("Margarita", gameState.getCocktailName());
        assertEquals(4, gameState.getAttemptsLeft());
        assertEquals("Incorrect guess.", gameState.getMessage());
        assertEquals(0, gameState.getCurrentScore());
        assertFalse(gameState.getRevealedHints().isEmpty()); // Hint revealed
    }

    @Test
    public void testMakeGuess_GameOver() {
        gameService.startGame();

        // Make incorrect guesses until game over
        for (int i = 0; i < 5; i++) {
            GuessRequest guessRequest = new GuessRequest();
            guessRequest.setGuess("Daiquiri");
            gameService.makeGuess(guessRequest);
        }

        GuessRequest finalGuessRequest = new GuessRequest();
        finalGuessRequest.setGuess("Another incorrect guess");

        GameState gameState = gameService.makeGuess(finalGuessRequest);

        assertEquals(0, gameState.getAttemptsLeft());
        assertEquals("The cocktail name was fully revealed. You're out of attempts! Game Ended.", gameState.getMessage());
        assertEquals("Margarita", gameState.getHiddenCocktailName()); // Full name revealed on game over
    }

    @Test
    public void testSkipRound() {
        gameService.startGame();

        GameState gameState = gameService.skipRound();

        assertEquals("Margarita", gameState.getCocktailName());
        assertEquals(4, gameState.getAttemptsLeft());
        assertEquals("You skipped the round. Here's a hint.", gameState.getMessage());
        assertEquals(0, gameState.getCurrentScore());
        assertFalse(gameState.getRevealedHints().isEmpty()); // Hint revealed
    }

}