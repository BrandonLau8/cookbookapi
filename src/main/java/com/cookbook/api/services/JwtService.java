package com.cookbook.api.services;

import com.auth0.jwt.algorithms.Algorithm;
import com.cookbook.api.models.UserEntity;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {

    public String generateToken(UserEntity userEntity);


}
