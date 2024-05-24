package com.cookbook.api.security;


import com.cookbook.api.repository.UserRepository;
import com.cookbook.api.services.JwtService;
import com.cookbook.api.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@ComponentScan
@EnableWebSecurity
public class SecurityConfig {
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtAuthFilter jwtAuthFilter;
    private final DaoAuthProvider daoAuthProvider;
    private final AuthEntryPoint authEntryPoint;
    private final CustomLogoutHandler customLogoutHandler;


    public SecurityConfig(CorsConfigurationSource corsConfigurationSource, JwtAuthFilter jwtAuthFilter, DaoAuthProvider daoAuthProvider, AuthEntryPoint authEntryPoint, CustomLogoutHandler customLogoutHandler) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.jwtAuthFilter = jwtAuthFilter;
        this.daoAuthProvider = daoAuthProvider;
        this.authEntryPoint = authEntryPoint;
        this.customLogoutHandler = customLogoutHandler;
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthProvider);
    }



    @Bean //manage the lifecycle of the bean.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                //meant to handle auth exceptions
                .exceptionHandling(exceptionHandling->
                        exceptionHandling.authenticationEntryPoint(authEntryPoint))



                //JWT Auth Filter
//                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)

                //JWT Request Filter
//                .addFilterAfter(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

//                //protect against cross site forgery using both sync token pattern or same site attribute.
//                //during dev, disabling helps
                .csrf(c -> c.disable()
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                )
//
                .cors((cors) -> cors
                        .configurationSource(corsConfigurationSource))

                //stateless means you do not wish to keep sessions on server. all requests from client require necessary info like tokens, etc...
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //usernamepassword authen filter
                .authorizeHttpRequests(requests -> requests
//                        //learn about mvcMatchers as well
                                .requestMatchers(HttpMethod.POST, "/login", "/register", "/refreshlogin").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/foods").permitAll()
                                .requestMatchers("/", "/test","/error", "/webjars/**").permitAll()
                                //any endpoint in your app requires that the security context at minimum be authen in order to allow it
                                .anyRequest().authenticated()
                )
                .logout(l->l
                      .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessUrl("/") // specify the URL to redirect to after logout
//                        .invalidateHttpSession(true) // invalidate the HttpSession
                        .deleteCookies("JSESSIONID") // delete cookies (if any)
                        .permitAll()
                );



        return http.build();
    }


}





