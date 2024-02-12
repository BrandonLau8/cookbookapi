package com.cookbook.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int quantity;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY) //NOT load relationship but not entire object
    @JoinColumn(name = "food_id")
    private Food food;
}
