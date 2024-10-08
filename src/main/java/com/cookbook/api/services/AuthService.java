package com.cookbook.api.services;

import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.RefreshTokenDto;
import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {
    public UserDto login(LoginDto loginDto);

    public UserDto register(RegisterDto registerDto);


}
