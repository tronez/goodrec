package com.goodrec.recipe.domain;

import com.goodrec.recipe.dto.IngredientDto;
import com.goodrec.recipe.dto.NewIngredientRequest;
import org.springframework.data.annotation.Id;

import java.util.UUID;

class Ingredient {

    @Id
    private UUID uuid;
    private String name;
    private Double amount;
    private Unit unit;

    public Ingredient(UUID uuid, String name, Double amount, Unit unit) {
        this.uuid = uuid;
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
        return new IngredientDto(uuid, name, amount, unit);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Unit getUnit() {
        return unit;
    }
}
