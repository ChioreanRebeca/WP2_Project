package com.locafy.locafy.config;

import com.locafy.locafy.security.CustomLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    public SecurityConfig(CustomLoginSuccessHandler customLoginSuccessHandler) {
        this.customLoginSuccessHandler = customLoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/images/**", "/", "/home", "/login", "/signup", "/css/**", "/js/**", "/contact").permitAll()
                        .requestMatchers("/locals-home", "/local-profile", "/favorites").hasRole("LOCAL")
                        .requestMatchers("/business-owner-home", "/business-owner-profile", "/businesses").hasRole("BUSINESS_OWNER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customLoginSuccessHandler)  //using the dynamic handler
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")              // <--- this is the URL Spring Security listens on
                        .logoutSuccessUrl("/?logout")            // <--- after logout, redirect to home page
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
}
