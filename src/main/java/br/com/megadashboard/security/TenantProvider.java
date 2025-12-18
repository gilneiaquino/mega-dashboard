package br.com.megadashboard.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class TenantProvider {

    public String getTenant(Authentication auth) {
        if (auth == null) throw new IllegalStateException("Sem autenticação");

        Object principal = auth.getPrincipal();
        if (principal instanceof Jwt jwt) {
            String tenant = jwt.getClaimAsString("tenant");
            if (tenant == null || tenant.isBlank()) {
                throw new IllegalStateException("Claim 'tenant' não encontrado no token");
            }
            return tenant;
        }
        throw new IllegalStateException("Principal não é Jwt (ajuste o SecurityConfig)");
    }
}
