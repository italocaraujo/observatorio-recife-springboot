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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        // Criando um usuário em memória com autenticação básica
        User.UserBuilder users = User.withUsername("usuario");
        return new InMemoryUserDetailsManager(
                users.password(passwordEncoder().encode("senha123"))
                        .roles("USER")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Para criptografar senhas
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/indicadores").authenticated()  // Exige login para acessar /api/indicadores
                                .anyRequest().permitAll()  // Outros endpoints são públicos
                )
                .httpBasic(withDefaults());  // Autenticação básica sem o método deprecated

        return http.build();
    }
}
