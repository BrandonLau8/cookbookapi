package com.cookbook.api.security;

import com.cookbook.api.models.RoleEntity;
import com.cookbook.api.models.RoleType;
import com.cookbook.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RoleInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    @Autowired
    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Seed initial roles if they don't exist
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            RoleEntity adminRole = new RoleEntity("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            RoleEntity userRole = new RoleEntity("ROLE_USER");
            roleRepository.save(userRole);
        }
    }
}
