//package com.cookbook.api.controllers;
//
//import com.cookbook.api.dto.AuthResponseDto;
//import com.cookbook.api.dto.LoginDto;
//import com.cookbook.api.dto.RegisterDto;
//import com.cookbook.api.models.Role;
//import com.cookbook.api.models.UserEntity;
//import com.cookbook.api.repository.RoleRepository;
//import com.cookbook.api.repository.UserRepository;
//import com.cookbook.api.security.JWTGenerator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Collection;
//import java.util.Collections;
//
//@RestController
//@RequestMapping("/api/auth/")
//public class AuthController {
//    private AuthenticationManager authenticationManager;
//    private UserRepository userRepository;
//    private RoleRepository roleRepository;
//    private PasswordEncoder passwordEncoder;
//    private JWTGenerator jwtGenerator;
//
//    @Autowired
//    public AuthController(
//            AuthenticationManager authenticationManager,
//            UserRepository userRepository,
//            RoleRepository roleRepository,
//            PasswordEncoder passwordEncoder,
//            JWTGenerator jwtGenerator) {
//        this.authenticationManager = authenticationManager;
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtGenerator = jwtGenerator;
//    }
//
//    @GetMapping("login")
//    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginDto.getUsername(), loginDto.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String token = jwtGenerator.generateToken(authentication);
//        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
//    }
//
//    @PostMapping("register")
//    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
//        if(userRepository.existsByUsername(registerDto.getUsername())) {
//            return new ResponseEntity<>("Username is taken", HttpStatus.BAD_REQUEST);
//        }
//
//        UserEntity user = new UserEntity();
//        user.setUsername(registerDto.getUsername());
//        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//        Role roles = roleRepository.findByName("USER").get();
//        user.setRoles(Collections.singletonList(roles));
//
//        userRepository.save(user);
//
//        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
//    }
//}
