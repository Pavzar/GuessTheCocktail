package com.ridango.pavzar.game.service;

import com.ridango.pavzar.game.entity.Cocktail;
import com.ridango.pavzar.game.model.CocktailResponse;
import com.ridango.pavzar.game.repository.CocktailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class CocktailService {

    @Value("${API_URI}")
    private String API_URI;

    private final CocktailRepository cocktailRepository;

    private static final Logger logger = LoggerFactory.getLogger(CocktailService.class);

    public CocktailService(CocktailRepository cocktailRepository) {
        this.cocktailRepository = cocktailRepository;
    }

    @Transactional
    public Cocktail getRandomCocktail() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<CocktailResponse> responseEntity = restTemplate.exchange(
                    API_URI, HttpMethod.GET, entity, CocktailResponse.class);

            CocktailResponse response = responseEntity.getBody();

            if (response != null && !response.getCocktail().isEmpty()) {
                Cocktail cocktail = response.getCocktail().get(0);
                try{
                    // Check if the cocktail ID already exists in the database
                    if (cocktailRepository.existsByIdDrink(cocktail.getIdDrink())) {
                        // If it exists, it is a duplicate, so fetch another one recursively
                        return getRandomCocktail();
                    } else {
                        cocktailRepository.save(cocktail);
                        return cocktail;
                    }
                } catch (DataIntegrityViolationException e) {
                    logger.warn("Duplicate cocktail detected. Fetching another one.", e);
                    return getRandomCocktail();
                }
            } else {
                logger.error("No cocktail found in the API response");
                return getRandomCocktail();
            }
        } catch (HttpClientErrorException.NotFound e) {
            logger.error("Cocktail not found", e);
            return getRandomCocktail();

        } catch (Exception e) {
            // Handle other exceptions (e.g., network issues, parsing errors)
            logger.error("Error fetching random cocktail", e);
            return getRandomCocktail();
        }
    }
}