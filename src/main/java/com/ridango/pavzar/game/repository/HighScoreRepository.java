package com.ridango.pavzar.game.repository;

import com.ridango.pavzar.game.entity.HighScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HighScoreRepository extends JpaRepository<HighScore, Long> {
}
