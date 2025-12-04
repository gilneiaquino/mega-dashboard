package br.com.megadashboard.repository;

import br.com.megadashboard.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByCodigoAndAtivoTrue(String codigo);
}
