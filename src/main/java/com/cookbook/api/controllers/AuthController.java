package com.cookbook.api.controllers;

import com.cookbook.api.dto.LoginDto;
import com.cookbook.api.dto.RegisterDto;
import com.cookbook.api.dto.UserDto;
import com.cookbook.api.security.UserAuthProvider;
import com.cookbook.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    private UserAuthProvider userAuthProvider;

    @Autowired
    private UserService userService;


    //For In-Memory User Details
//    @PostMapping("/login")
//    public ResponseEntity<Authentication> login(@RequestBody LoginRequest loginRequest) {
//        Authentication authenticationRequest =
//                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
//        Authentication authenticationResponse =
//                this.authenticationManager.authenticate(authenticationRequest);
//        // ...
//        return new ResponseEntity<>(authenticationRequest, HttpStatus.OK);
//    }

//    public record LoginRequest(String username, String password) {
//    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto) {
        UserDto userDto = userService.login(loginDto);
        userDto.setToken(userAuthProvider.createToken(userDto.getUsername()));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterDto registerDto) {
        UserDto createdUser = userService.register(registerDto);
        createdUser.setToken(userAuthProvider.createToken(registerDto.getUsername()));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }
}




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
