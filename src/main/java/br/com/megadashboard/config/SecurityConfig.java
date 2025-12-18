package br.com.megadashboard.config;

import br.com.megadashboard.repository.UsuarioRepository;
import br.com.megadashboard.security.TenantFilter;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String SECRET =
            "trocar-por-uma-chave-grande-bem-segura-256-bits";

    private final TenantFilter tenantFilter;
    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(TenantFilter tenantFilter, UsuarioRepository usuarioRepository) {
        this.tenantFilter = tenantFilter;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(tenantFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    // ✅ Para o TenantProvider funcionar (principal Jwt)
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    // ✅ Encoder para comparar senhas (se você já salva bcrypt no banco)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ✅ UserDetailsService (busca usuário no banco)
     * Observação: autenticação por login/senha normalmente não precisa filtrar por tenant aqui,
     * porque seu AuthController já valida tenant depois. Se quiser 100% tenant-aware,
     * dá pra buscar tenant via TenantContext aqui (eu te ajusto).
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByLoginAndAtivoTrue(username)
                .map(u -> (UserDetails) org.springframework.security.core.userdetails.User
                        .withUsername(u.getLogin())
                        .password(u.getSenha()) // precisa ser hash bcrypt
                        .roles(u.getPerfil().name()) // ou .authorities(...)
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    // ✅ Provider padrão de autenticação (login/senha)
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // ✅ Aqui nasce o AuthenticationManager que seu AuthController injeta
    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider daoAuthenticationProvider) {
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowCredentials(true);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
