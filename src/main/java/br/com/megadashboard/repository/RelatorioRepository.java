package br.com.megadashboard.repository;

import br.com.megadashboard.model.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {

    List<Relatorio> findAllByTenant_CodigoOrderByNomeAsc(String tenantCodigo);

    Optional<Relatorio> findByIdAndTenant_Codigo(Long id, String tenantCodigo);
}
