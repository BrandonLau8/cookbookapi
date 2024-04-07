package com.cookbook.api.services;

import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.models.UserEntity;
import com.cookbook.api.services.impl.UserServiceImpl;

public interface UserService {
    public UserDto login(LoginDto loginDto);
}
