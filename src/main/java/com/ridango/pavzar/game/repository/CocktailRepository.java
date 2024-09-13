package com.ridango.pavzar.game.repository;

import com.ridango.pavzar.game.entity.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Long>
{
    boolean existsByIdDrink(Long aLong);
}
