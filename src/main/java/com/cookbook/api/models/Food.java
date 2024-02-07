package com.cookbook.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //writes out constructor with all arguments
@NoArgsConstructor //no argument constructor, tbd how it works
@Entity //mapped to database tables
public class Food {
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //create unique ids
    private int id;
    private String name;
}
