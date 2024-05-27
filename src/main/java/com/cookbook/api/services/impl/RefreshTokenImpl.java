//package com.cookbook.api.services.impl;
//
//import com.cookbook.api.models.RefreshToken;
//import com.cookbook.api.repository.RefreshTokenRepository;
//import com.cookbook.api.repository.UserRepository;
//import com.cookbook.api.services.RefreshTokenService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class RefreshTokenImpl implements RefreshTokenService {
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public RefreshTokenImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
//        this.refreshTokenRepository = refreshTokenRepository;
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public RefreshToken createRefeshToken(String username) {
//
//        RefreshToken refreshToken = RefreshToken.builder()
//                .person(userRepository.findByUsername(username)
//                        .orElseThrow(()->new UsernameNotFoundException("Username not found")))
//                .token(UUID.randomUUID().toString())
//                .expiryDate(Instant.now().plusMillis(600000)) //10mins
//                .build();
//        return refreshTokenRepository.save(refreshToken);
//    }
//
//    @Override
//    public Optional<RefreshToken> findByToken(String token) {
//        return refreshTokenRepository.findByToken(token);
//    }
//
//    @Override
//    public RefreshToken verifyExpiration(String token) {
//        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
//                .orElseThrow(() ->new RuntimeException("Refresh token not found"));
//        if(refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
//            refreshTokenRepository.delete(refreshToken);
//            throw new RuntimeException(refreshToken.getToken() + "Refresh token expired. Please make a new signin request");
//        }
//        return refreshToken;
//    }
//}
