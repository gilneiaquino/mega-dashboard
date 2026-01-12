package br.com.megadashboard.service;

import br.com.megadashboard.controller.dto.dashboard.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DashboardService {

    // ===== CRUD / METADADOS =====

    DashboardResponse criar(DashboardRequest request, String tenantCodigo);

    DashboardResponse buscarPorId(Long id, String tenantCodigo);

    Page<DashboardResumoResponse> listar(String tenantCodigo, String nome, Pageable pageable);

    // ===== RENDERIZAÇÃO =====

    DashboardRenderResponse render(Long dashboardId, String tenantCodigo);

    // ===== RESUMO (GRÁFICOS HOME) =====

    DashboardResumoRenderResponse resumo(
            String tenantCodigo,
            String dataInicio,
            String dataFim,
            String categoria,
            String status
    );
}
