package br.com.megadashboard.controller;

import br.com.megadashboard.controller.dto.LoginRequest;
import br.com.megadashboard.controller.dto.LoginResponse;
import br.com.megadashboard.controller.dto.UsuarioResponse;
import br.com.megadashboard.model.Usuario;
import br.com.megadashboard.repository.UsuarioRepository;
import br.com.megadashboard.security.JwtService;
import br.com.megadashboard.security.TenantContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200") // opcional se já tiver CORS global
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        String tenant = TenantContext.getTenant();
        if (tenant == null) {
            return ResponseEntity.badRequest().build(); // ou mensagem bonita
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.login(),
                        request.senha()
                )
        );

        Usuario usuario = usuarioRepository
                .findByLoginAndTenant_CodigoAndAtivoTrue(request.login(), tenant)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String token = jwtService.gerarToken(usuario, tenant);

        UsuarioResponse usuarioResponse = new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getPerfil(),
                tenant
        );

        return ResponseEntity.ok(new LoginResponse(token, usuarioResponse));
    }
}
