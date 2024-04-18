package com.cookbook.api.models;

import com.auth0.jwt.algorithms.Algorithm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;

    private boolean isLoggedOut;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private UserEntity person;

//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "person_token",
//            joinColumns= @JoinColumn(name="token_id", referencedColumnName="id"),
//            inverseJoinColumns= @JoinColumn(name="person_id", referencedColumnName="id")
//    )
//    private UserEntity person;
}
