package com.cookbook.api.security;

import com.cookbook.api.repository.TokenRepository;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    private final UserService userService;

    private final PasswordConfig passwordConfig;

    private final JwtAuthFilter jwtAuthFilter;

    private final JwtService jwtService;

    private final CustomLogoutHandler logoutHandler;

    private final SecretKeyGenerator secretKeyGenerator;

    private final TokenRepository tokenRepository;

//    private final AuthEntryPoint authEntryPoint;



    @Bean //manage the lifecycle of the bean.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                //ability to have exception handling
//                .exceptionHandling(exceptionHandling->
//                        exceptionHandling.authenticationEntryPoint(authEntryPoint))

                .addFilterBefore(new JwtAuthFilter(jwtService, secretKeyGenerator, tokenRepository), BasicAuthenticationFilter.class)
//                //protect against cross site forgery using both sync token pattern or same site attribute.
//                //during dev, disabling helps
                .csrf(csrf->csrf.disable())

                .cors((cors) -> cors
                        .configurationSource(corsConfigurationSource))
//
//
//
//                //stateless means you do not wish to create sessions which is related to logging users out.
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //usernamepassword authen filter
                .authorizeHttpRequests(requests -> requests
//                        //learn about mvcMatchers as well
//                        .requestMatchers("/api/**").hasAuthority("USER")
//                        .requestMatchers("/api/food/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/register").permitAll()
                                .requestMatchers("/api/foods").permitAll()

                                //any endpoint in your app requires that the security context at minimum be authen in order to allow it
                                .anyRequest().authenticated()
                )
                .logout(l->l
                        .logoutUrl("/api/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                );


        return http.build();
    }


//    @Bean
//    public AuthenticationManager authenticationManager(
//            UserService userService,
//            PasswordConfig passwordConfig) {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userService);
//        authenticationProvider.setPasswordEncoder(passwordConfig.passwordEncoder());
//
//        return new ProviderManager(authenticationProvider);
//    }

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/cookbookapi"); // Update with your PostgreSQL URL
//        dataSource.setUsername("postgres"); // Update with your PostgreSQL username
//        dataSource.setPassword("password"); // Update with your PostgreSQL password
//        return dataSource;
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.builder()
//                .username("user")
//                   .password(passwordEncoder().encode("password"))
//                .roles("USER")
//                .build();
//        UserDetails adminDetails = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("password"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(userDetails, adminDetails);
//    }

}





