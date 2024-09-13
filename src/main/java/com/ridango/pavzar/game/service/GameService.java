package com.ridango.pavzar.game.service;

import com.ridango.pavzar.game.entity.Cocktail;
import com.ridango.pavzar.game.model.GameState;
import com.ridango.pavzar.game.model.GuessRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
public class GameService {
    private final CocktailService cocktailService;
    private final HighScoreService highScoreService;

    public GameService(CocktailService cocktailService, HighScoreService highScoreService) {
        this.cocktailService = cocktailService;
        this.highScoreService = highScoreService;
    }

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private static final int MAX_ATTEMPTS = 5;
    private int currentScore = 0;
    private int highScore;
    private Cocktail currentCocktail;
    private String cocktailName;
    private String hiddenCocktailName;
    private String instructions;
    private int letterCount;
    private int attemptsLeft;
    private final List<String> revealedHints = new ArrayList<>();


    public GameState startGame() {
        // Initialize game state
        currentCocktail = cocktailService.getRandomCocktail();
        cocktailName = currentCocktail.getStrDrink();
        letterCount = cocktailName.length();
        hiddenCocktailName = cocktailName.replaceAll("[^ ]", "_");
        instructions = currentCocktail.getStrInstructions();
        highScore = highScoreService.getHighScore();
        currentScore = 0;
        attemptsLeft = MAX_ATTEMPTS;
        revealedHints.clear();

        return buildGameState("Good Luck.");
    }

    public GameState makeGuess(GuessRequest guessRequest) {
        if (cocktailName == null) {
            return buildGameState("Please start a new game first.");
        }

        String guess = guessRequest.getGuess();

        if (guess == null || guess.isBlank()) {
            return buildGameState("Please enter a valid guess.");
        }

        if (isCocktailNameFullyRevealed()) {
            attemptsLeft = 0;
            updateHighScoreIfNeeded();
            return buildGameState("The cocktail name was fully revealed. You're out of attempts! Game Ended.");
        } else if (correctGuess(guess)) {
            currentScore += attemptsLeft;
            return startNewGame();
        } else {
            attemptsLeft--;
            if (attemptsLeft == 0) {
                updateHighScoreIfNeeded();
                hiddenCocktailName = cocktailName;
                return buildGameState("Game Ended.");
            } else {
                revealHint();
                // if word is fully revealed after the revealHint method, end the game
                if (isCocktailNameFullyRevealed()) {
                    attemptsLeft = 0;
                    updateHighScoreIfNeeded();
                    return buildGameState("The cocktail name was fully revealed. You're out of attempts! Game Ended.");
                }
                return buildGameState("Incorrect guess.");
            }
        }
    }

    public GameState skipRound() {
        attemptsLeft--;
        if (attemptsLeft == 0) {
            updateHighScoreIfNeeded();
            hiddenCocktailName = cocktailName;
            return buildGameState("Game Ended.");
        } else {
            revealHint();

            if (isCocktailNameFullyRevealed()) {
                attemptsLeft = 0;
                updateHighScoreIfNeeded();
                return buildGameState("The cocktail name was fully revealed. You're out of attempts! Game Ended.");
            }

            return buildGameState("You skipped the round. Here's a hint.");
        }
    }

    private GameState buildGameState(String message) {
        return new GameState(
                cocktailName,
                hiddenCocktailName,
                attemptsLeft,
                instructions,
                revealedHints,
                message,
                highScore,
                currentScore
        );
    }

    private GameState startNewGame() {
        currentCocktail = cocktailService.getRandomCocktail();
        cocktailName = currentCocktail.getStrDrink();
        instructions = currentCocktail.getStrInstructions();
        letterCount = cocktailName.length();
        hiddenCocktailName = cocktailName.replaceAll("[^ ]", "_");
        attemptsLeft = MAX_ATTEMPTS;
        revealedHints.clear();
        return buildGameState("Correct guess.");
    }

    private void updateHighScoreIfNeeded() {
        if (currentScore > highScore) {
            highScore = currentScore;
            highScoreService.updateHighScore(highScore);
        }
    }

    private boolean correctGuess(String guess) {
        return guess.equalsIgnoreCase(cocktailName);
    }

    private boolean isCocktailNameFullyRevealed() {
        return !hiddenCocktailName.contains("_");
    }

    private void revealHint() {
        // Reveal letters (more for longer cocktails)
        int lettersToReveal = Math.max(1, cocktailName.length() / 5);
        revealRandomLetters(lettersToReveal);

        // Reveal a random piece of additional info about the cocktail
        List<String> possibleHints = new ArrayList<>();

        // Add hints only if the corresponding fields are not null or empty
        if (currentCocktail.getStrCategory() != null && !currentCocktail.getStrCategory().isEmpty()) {
            possibleHints.add("Category: " + currentCocktail.getStrCategory());
        }

        if (currentCocktail.getStrGlass() != null && !currentCocktail.getStrGlass().isEmpty()) {
            possibleHints.add("Glass: " + currentCocktail.getStrGlass());
        }

        if (currentCocktail.getStrDrinkThumb() != null && !currentCocktail.getStrDrinkThumb().isEmpty()) {
            possibleHints.add(currentCocktail.getStrDrinkThumb());
        }

        // Add ingredient hints only if they are available
        for (int i = 1; i <= 4; i++) {
            String ingredient = currentCocktail.getStrIngredient(i);
            if (ingredient != null && !ingredient.isEmpty()) {
                possibleHints.add("Ingredient " + i + ": " + ingredient);
            }
        }

        if (!possibleHints.isEmpty()) {
            // Create a copy of possibleHints to avoid modifying the original list
            List<String> shuffledHints = new ArrayList<>(possibleHints);
            Collections.shuffle(shuffledHints);

            // Find the first hint that hasn't been revealed yet
            for (String hint : shuffledHints) {
                if (!revealedHints.contains(hint)) {
                    revealedHints.add(hint);
                    return;
                }
            }

            // If we reach here, it means all possible hints have already been revealed
            logger.warn("All possible hints have been revealed. Revealing a random letter instead.");
            revealRandomLetter();
        } else {
            logger.warn("No other hints available at all");
            revealRandomLetter();
        }
    }


    private void revealRandomLetters(int count) {
        for (int i = 0; i < count; i++) {
            revealRandomLetter();
        }
    }

    private void revealRandomLetter() {
        // Get a list of all unrevealed indices
        List<Integer> unrevealedIndices = IntStream.range(0, cocktailName.length())
                .filter(i -> hiddenCocktailName.charAt(i) == '_')
                .boxed()
                .toList();

        if (!unrevealedIndices.isEmpty()) {
            // Choose a random unrevealed index
            int randomIndex = new Random().nextInt(unrevealedIndices.size());
            int indexToReveal = unrevealedIndices.get(randomIndex);

            char[] hiddenChars = hiddenCocktailName.toCharArray();
            hiddenChars[indexToReveal] = cocktailName.charAt(indexToReveal);
            hiddenCocktailName = new String(hiddenChars);
        } else {
            logger.warn("All letters are already revealed.");
        }
    }
}
