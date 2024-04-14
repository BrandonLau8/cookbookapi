package com.cookbook.api.services;

import com.cookbook.api.dto.AuthResponseDto;
import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;

public interface AuthService {
    public UserDto login(LoginDto loginDto);

    AuthResponseDto register(RegisterDto registerDto);
}
