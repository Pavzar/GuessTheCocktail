package com.ridango.pavzar.game.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idDrink;

    private String strDrink;

    private String strCategory;

    @Lob
    private String strInstructions;

    private String strGlass;

    private String strDrinkThumb;

    private String strIngredient1;

    private String strIngredient2;

    private String strIngredient3;

    private String strIngredient4;

    private String strMeasure1;

    private String strMeasure2;

    private String strMeasure3;

    private String strMeasure4;

    public String getStrIngredient(int index) {
        return switch (index) {
            case 1 -> strIngredient1;
            case 2 -> strIngredient2;
            case 3 -> strIngredient3;
            case 4 -> strIngredient4;
            default -> null;
        };
    }
}