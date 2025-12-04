package br.com.megadashboard.config; // ajusta para o seu pacote

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CORS usa o bean corsConfigurationSource()
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // MUITO IMPORTANTE: liberar OPTIONS para o preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // libera login
                        .requestMatchers("/auth/login").permitAll()
                        // TODO: se tiver Swagger, health, etc, libera aqui também
                        .anyRequest().authenticated()
                );

        // depois você adiciona filtro JWT aqui, se já tiver
        // http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Em dev: libera só o Angular
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        // Se for usar credenciais (cookies), mantenha true.
        config.setAllowCredentials(true);

        // Métodos permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers que o front pode mandar
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Cache-Control",
                "Content-Type",
                "X-Requested-With",
                "X-Tenant-ID"
        ));

        // Headers que o browser pode ler da resposta
        config.setExposedHeaders(List.of(
                "Authorization"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // aplica para todas as rotas
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
