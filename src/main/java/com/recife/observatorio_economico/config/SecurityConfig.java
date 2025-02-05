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

    // Configuração CORS Genérica
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aceitar qualquer rota
                        .allowedOrigins(
                            "http://localhost:3000", 
                            "https://observatorio-recife.vercel.app", 
                            "https://observatorio-economico-recife.vercel.app",
                            "http://192.168.244.57:3000"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);
            }
        };
    }

    // Configuração de Usuário Genérica (autenticação em memória)
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("italo")
                    .password(passwordEncoder().encode("observatorio"))
                    .roles("USER")
                    .build()
        );
    }

    // Gerenciamento de senha
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Segurança Genérica para todas as rotas /api/v1/**
    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())  // CORS habilitado
                .csrf(csrf -> csrf.disable())  // Desabilitar CSRF para APIs REST
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/**").authenticated()  // Permitir autenticação para todas as rotas /api/v1/*
                                .anyRequest().permitAll()  // Outras rotas sem restrição
                )
                .httpBasic(withDefaults());  // Autenticação básica

        return http.build();
    }
}
