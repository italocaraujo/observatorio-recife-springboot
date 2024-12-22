package com.recife.observatorio_economico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "https://observatorio-recife.vercel.app", "http://192.168.1.57:3000") // Ajuste as origens
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true); // Permite envio de cookies/credenciais
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Usuário em memória para testes e desenvolvimento
        User.UserBuilder users = User.withUsername("italo");
        return new InMemoryUserDetailsManager(
                users.password(passwordEncoder().encode("observatorio"))
                        .roles("USER")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // Ativa CORS conforme configurado no WebMvcConfigurer
                .csrf(csrf -> csrf.disable()) // Desativa CSRF para APIs REST
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/aeroporto/**").authenticated() // Protege endpoints do aeroporto
                                .anyRequest().permitAll() // Outros endpoints públicos
                )
                .httpBasic(withDefaults()); // Configuração de autenticação básica

        return http.build();
    }
}
