package com.goodrec.recipe.dto;

import com.goodrec.recipe.domain.Unit;

public class NewIngredientRequest {

    private String name;
    private Double amount;
    private Unit unit;

    public NewIngredientRequest(String name, Double amount, Unit unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
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
