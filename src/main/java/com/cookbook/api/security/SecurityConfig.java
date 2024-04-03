package com.cookbook.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    private JwtAuthEntryPoint jwtAuthEntryPoint;
//    private CustomUserDetailsService userDetailsService;

    @Bean //manage the lifecycle of the bean.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                //protect against cross site forgery using both sync token pattern or same site attribute.
//                //during dev, disabling helps
//                .csrf(csrf->csrf.disable())
//
//                //ability to have exception handling
//                .exceptionHandling(exceptionHandling->
//                        exceptionHandling.authenticationEntryPoint(jwtAuthEntryPoint))
//
//                //stateless means you do not wish to create sessions which is related to logging users out.
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //usernamepassword authen filter
                .authorizeHttpRequests(authorize -> authorize
//                        //learn about mvcMatchers as well
//                        .requestMatchers("/api/**").hasAuthority("USER")
//                        .requestMatchers("/api/food/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        //any endpoint in your app requires that the security context at minimum be authen in order to allow it
                        .anyRequest().authenticated()
                );
        return http.build();
    }


//    @Autowired
//    public SecurityConfig(
//            CustomUserDetailsService userDetailsService,
//            JwtAuthEntryPoint jwtAuthEntryPoint) {
//        this.userDetailsService = userDetailsService;
//        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
//    }

    //Core Interface that provides the ability to authenticate a user.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public JWTAuthenticationFilter jwtAuthenticationFilter() {
//        return new JWTAuthenticationFilter();
//    }
}
