package com.goodrec.recipe;

import org.springframework.data.annotation.Id;

import java.util.UUID;

class Ingredient {

    @Id
    private UUID uuid;
    private String name;
    private Double amount;
    private Unit unit;

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
