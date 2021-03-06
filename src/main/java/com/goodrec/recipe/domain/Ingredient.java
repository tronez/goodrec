package com.goodrec.recipe.domain;

import com.goodrec.recipe.dto.IngredientDto;
import org.springframework.data.annotation.Id;

import java.util.UUID;

class Ingredient {

    @Id
    private UUID id;
    private String name;
    private Double amount;
    private Unit unit;

    Ingredient(UUID id, String name, Double amount, Unit unit) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    static Ingredient createFrom(IngredientDto dto) {
        return new Ingredient(
                dto.getUuid(),
                dto.getName(),
                dto.getAmount(),
                dto.getUnit()
        );
    }

    IngredientDto toDto() {
        return new IngredientDto(id, name, amount, unit);
    }

    UUID getId() {
        return id;
    }

    String getName() {
        return name;
    }

    Double getAmount() {
        return amount;
    }

    Unit getUnit() {
        return unit;
    }
}
