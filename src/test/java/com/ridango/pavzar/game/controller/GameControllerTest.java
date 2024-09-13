package com.ridango.pavzar.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridango.pavzar.game.model.GameState;
import com.ridango.pavzar.game.model.GuessRequest;
import com.ridango.pavzar.game.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testStartGame() throws Exception {
        GameState mockGameState = new GameState(
                "Margarita",
                "______",
                5,
                "Instructions...",
                new ArrayList<>(),
                "Good Luck.",
                10,
                0
        );
        when(gameService.startGame()).thenReturn(mockGameState);

        mockMvc.perform(get("/api/start"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockGameState)));
    }

    @Test
    public void testMakeGuess() throws Exception {
        GuessRequest guessRequest = new GuessRequest();
        guessRequest.setGuess("Margarita");
        GameState mockGameState = new GameState(
                "Margarita",
                "Margarita",
                0,
                "Instructions...",
                new ArrayList<>(),
                "Correct guess.",
                10,
                5
        );
        when(gameService.makeGuess(guessRequest)).thenReturn(mockGameState);

        mockMvc.perform(post("/api/guess")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guessRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockGameState)));
    }

    @Test
    public void testSkipRound() throws Exception {
        GameState mockGameState = new GameState(
                "Margarita",
                "M_____",
                4,
                "Instructions...",
                List.of("Category: Ordinary Drink"),
                "You skipped the round. Here's a hint.",
                10,
                0
        );
        when(gameService.skipRound()).thenReturn(mockGameState);

        mockMvc.perform(get("/api/skip"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockGameState)));
    }
}