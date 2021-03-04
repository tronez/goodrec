package com.goodrec.recipe.dto;

import com.goodrec.recipe.domain.Unit;

import java.util.UUID;

public class IngredientDto {

    private UUID uuid;
    private String name;
    private Double amount;
    private Unit unit;

    public IngredientDto() {
    }

    public IngredientDto(UUID uuid, String name, Double amount, Unit unit) {
        this.uuid = uuid;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public static IngredientDto createFrom(NewIngredientRequest newIngredient) {
        return new IngredientDto(
                UUID.randomUUID(),
                newIngredient.getName(),
                newIngredient.getAmount(),
                newIngredient.getUnit()
        );
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
