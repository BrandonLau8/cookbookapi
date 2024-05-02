package com.cookbook.api.security;

import com.cookbook.api.models.UserEntity;
import com.cookbook.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findById(username);

        if(userEntity.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found");
        }
        return new UserDetailsImpl(userEntity.get());
    }
}
