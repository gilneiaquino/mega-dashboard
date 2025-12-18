package br.com.megadashboard.repository;

import br.com.megadashboard.model.Dashboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {

    Optional<Dashboard> findByIdAndTenantCodigo(Long id, String tenantCodigo);

    Page<Dashboard> findByTenantCodigo(String tenantCodigo, Pageable pageable);

    Page<Dashboard> findByTenantCodigoAndNomeContainingIgnoreCase(
            String tenantCodigo, String nome, Pageable pageable
    );
}
