package br.com.megadashboard.controller;

import br.com.megadashboard.controller.dto.dashboard.*;
import br.com.megadashboard.security.TenantContext;
import br.com.megadashboard.service.DashboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboards")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Criar dashboard
     */
    @PostMapping
    public DashboardResponse criar(@RequestBody DashboardRequest request) {
        String tenant = TenantContext.getTenant();
        return dashboardService.criar(request, tenant);
    }

    /**
     * Buscar dashboard por ID
     */
    @GetMapping("/{id}")
    public DashboardResponse buscarPorId(@PathVariable Long id) {
        String tenant = TenantContext.getTenant();
        return dashboardService.buscarPorId(id, tenant);
    }

    /**
     * Listar dashboards (resumido)
     */
    @GetMapping
    public Page<DashboardResumoResponse> listar(
            @RequestParam(required = false) String nome,
            Pageable pageable
    ) {
        String tenant = TenantContext.getTenant();
        return dashboardService.listar(tenant, nome, pageable);
    }
 
    @GetMapping("/{id}/render")
    public DashboardRenderResponse render(@PathVariable Long id) {
        String tenant = TenantContext.getTenant();

        // Mock inicial (Sprint 4): retorna cards fixos já no formato final
        return DashboardRenderResponse.builder()
                .dashboardId(id)
                .titulo("Visão Geral (" + tenant + ")")
                .cards(List.of(
                        CardResponse.builder()
                                .id("kpi_total")
                                .tipo(CardTipo.KPI)
                                .titulo("Total")
                                .ordem(1)
                                .colSpan(3)
                                .data(Map.of(
                                        "valor", 12345,
                                        "variacaoPct", 2.3
                                ))
                                .build(),

                        CardResponse.builder()
                                .id("grafico_carteira")
                                .tipo(CardTipo.CHART_BAR)
                                .titulo("Carteira por Situação")
                                .ordem(2)
                                .colSpan(6)
                                .data(Map.of(
                                        "labels", List.of("Ativo", "Atraso", "Liquidado"),
                                        "series", List.of(
                                                Map.of("name", "Qtd", "values", List.of(10, 5, 20))
                                        )
                                ))
                                .build()
                ))
                .build();
    }

}
