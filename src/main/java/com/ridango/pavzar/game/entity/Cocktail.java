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

    public Cocktail(Object o, long l, String unknown, String unknown1, String noInstructionsAvailable, String unknown2, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
    }

    public static Cocktail defaultCocktail() {
        return new Cocktail(null, 0L, "Unknown", "Unknown", "No instructions available",
                "Unknown", null, null, null, null, null, null, null, null);
    }

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