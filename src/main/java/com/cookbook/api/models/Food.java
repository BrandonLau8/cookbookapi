package com.cookbook.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor //writes out constructor with all arguments
@NoArgsConstructor //no argument constructor, tbd how it works
@Entity //mapped to database tables
public class Food {
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //create unique ids
    private int id;
    private String name;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients = new ArrayList<Ingredient>();

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<Review>();
}
