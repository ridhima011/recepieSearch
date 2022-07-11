package com.recepies.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.recepies.model.foodType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class recepie {
    @Id
    private String id;
    private String dishName;
    private foodType type;
    private List<String> ingredients;
    private List<String> instructions;
    private LocalDateTime created;
    private Integer servings;

    public recepie(String dishName,
                   foodType type,
                   List<String> ingredients,
                   List<String> instructions,
                   Integer servings) {
        this.dishName = dishName;
        this.type = type;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.created = LocalDateTime.now();
        this.servings = servings; 
    }
}
