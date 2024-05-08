package com.cookbook.api.security;


import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CorsConfigurationSource corsConfigurationSource;
    private final UserService userService;
    private final PasswordConfig passwordConfig;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtService jwtService;
    private final SecretKeyGenerator secretKeyGenerator;
    private final DaoAuthProvider daoAuthProvider;
    private final UserRepository userRepository;

    private final UserDetailsServiceImpl userDetailsService;


    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, CorsConfigurationSource corsConfigurationSource, UserService userService, PasswordConfig passwordConfig, JwtAuthFilter jwtAuthFilter, JwtService jwtService, SecretKeyGenerator secretKeyGenerator, DaoAuthProvider daoAuthProvider, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.corsConfigurationSource = corsConfigurationSource;
        this.userService = userService;
        this.passwordConfig = passwordConfig;
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtService = jwtService;
        this.secretKeyGenerator = secretKeyGenerator;
        this.daoAuthProvider = daoAuthProvider;
        this.userRepository = userRepository;
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordConfig.passwordEncoder());
        auth.authenticationProvider(daoAuthProvider);
    }


    @Bean //manage the lifecycle of the bean.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                //ability to have exception handling
//                .exceptionHandling(exceptionHandling->
//                        exceptionHandling.authenticationEntryPoint(authEntryPoint))

                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
//                //protect against cross site forgery using both sync token pattern or same site attribute.
//                //during dev, disabling helps
                .csrf(csrf -> csrf.disable())

                .cors((cors) -> cors
                        .configurationSource(corsConfigurationSource))
//
//
//
//                //stateless means you do not wish to create sessions which is related to logging users out.
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailsService)

                //usernamepassword authen filter
                .authorizeHttpRequests(requests -> requests
//                        //learn about mvcMatchers as well
//                        .requestMatchers("/api/**").hasAuthority("USER")
//                        .requestMatchers("/api/food/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/register").permitAll()
                                .requestMatchers("/api/foods").permitAll()

                                //any endpoint in your app requires that the security context at minimum be authen in order to allow it
                                .anyRequest().authenticated()
                );
//                .logout(l->l
//                        .logoutUrl("/api/logout")
//                        .addLogoutHandler(logoutHandler)
//                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
//                );


        return http.build();
    }

}





