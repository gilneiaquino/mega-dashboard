package br.com.megadashboard.security;

import br.com.megadashboard.model.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    // depois joga isso em configuração / variável de ambiente
    private static final String SECRET = "trocar-por-uma-chave-grande-bem-segura-256-bits";
    private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1h

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String gerarToken(Usuario usuario, String tenantCodigo) {
        return Jwts.builder()
                .setSubject(usuario.getLogin())
                .addClaims(Map.of(
                        "nome", usuario.getNome(),
                        "perfil", usuario.getPerfil().name(),
                        "tenant", tenantCodigo
                ))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extrairLogin(String token) {
        return getClaims(token).getSubject();
    }

    public boolean tokenValido(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
