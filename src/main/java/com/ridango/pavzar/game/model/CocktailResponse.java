package com.ridango.pavzar.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ridango.pavzar.game.entity.Cocktail;
import lombok.Data;

import java.util.List;

@Data
public class CocktailResponse {
    @JsonProperty("drinks")
    private List<Cocktail> cocktail;
}