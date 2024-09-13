package com.ridango.pavzar.game.service;

import com.ridango.pavzar.game.entity.HighScore;
import com.ridango.pavzar.game.repository.HighScoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HighScoreService {

    private final HighScoreRepository highScoreRepository;

    public HighScoreService(HighScoreRepository highScoreRepository) {
        this.highScoreRepository = highScoreRepository;
    }

    public int getHighScore() {
        return highScoreRepository.findAll().stream()
                .findFirst()
                .map(HighScore::getHighScore)
                .orElse(0);
    }

    public void updateHighScore(int newHighScore) {
        List<HighScore> allHighScores = highScoreRepository.findAll();

        if (allHighScores.isEmpty()) {
            // If no high score exists yet, create a new one
            highScoreRepository.save(new HighScore(null, newHighScore));
        } else {
            // Update the existing high score
            HighScore existingHighScore = allHighScores.get(0);
            existingHighScore.setHighScore(newHighScore);
            highScoreRepository.save(existingHighScore);
        }
    }
}
