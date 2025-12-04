package br.com.megadashboard.repository;

import br.com.megadashboard.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // login + tenant.codigo
    Optional<Usuario> findByLoginAndTenant_CodigoAndAtivoTrue(String login, String tenantCodigo);
}
