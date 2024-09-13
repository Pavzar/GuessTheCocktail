# About the Project
## This project is a cocktail-themed guessing game built with Spring Boot for the backend and Angular for the frontend.

### Backend (Spring Boot): Handles the game logic, stores high scores, and provides RESTful APIs for the frontend to interact with.
### Frontend (Angular): Presents a user-friendly interface where players can guess the cocktail name, receive hints, and see their score.

#### Cocktails are fetched from https://www.thecocktaildb.com/api.php

- Uses Java 17

Start the CocktailGameApplication and go to localhost:8080

### Gameplay
- Press "New Game"
- A masked cocktail name and instructions will be displayed.
- Enter your guess in the input field and click "Guess".
- If you need a hint, click "Skip Round".
- You have a limited number of attempts to guess the cocktail name.
- Your score increases with each correct guess.
- After an incorrect guess, some random letters in the cocktail name will be revealed. (e.g., "_ _ _ _ _ " -> " _ j _ _ " -> " _ j _ _ o" -> "M _ j _ _ o" -> "M _ ji _ o" -> "M _ jito" -> "Mojito")
- Additional information about the cocktail will also be revealed after an incorrect guess. (e.g., category, glass, ingredients, picture)

