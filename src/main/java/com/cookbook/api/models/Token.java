package com.cookbook.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String regToken;

    private String loginToken;

    private boolean isLoggedOut;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_tokens",
            joinColumns= @JoinColumn(name="token_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="user_id", referencedColumnName="id")
    )
    private UserEntity username;
}
