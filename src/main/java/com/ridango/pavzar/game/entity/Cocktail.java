package com.ridango.pavzar.game.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public static Cocktail getDefaultCocktail() {
        // Create a new Cocktail object with default values
        Cocktail defaultCocktail = new Cocktail();
        defaultCocktail.setIdDrink(1L);
        defaultCocktail.setStrDrink("Mojito");
        defaultCocktail.setStrCategory("Cocktail");
        defaultCocktail.setStrInstructions("Muddle mint leaves with sugar and lime juice. Add rum and top with soda water. Garnish with mint sprig.");
        defaultCocktail.setStrGlass("Highball glass");
        defaultCocktail.setStrDrinkThumb("https://www.thecocktaildb.com/images/media/drink/metwgh1606770327.jpg");
        defaultCocktail.setStrIngredient1("White rum");
        defaultCocktail.setStrIngredient2("Sugar");
        defaultCocktail.setStrIngredient3("Lime juice");
        defaultCocktail.setStrIngredient4("Soda water");
        defaultCocktail.setStrMeasure1("2 oz");
        defaultCocktail.setStrMeasure2("2 tsp");
        defaultCocktail.setStrMeasure3("1 oz");
        defaultCocktail.setStrMeasure4("Top");

        return defaultCocktail;
    }
}