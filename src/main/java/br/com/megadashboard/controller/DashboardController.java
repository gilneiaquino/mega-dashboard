package br.com.megadashboard.controller;

import br.com.megadashboard.controller.dto.dashboard.*;
import br.com.megadashboard.security.TenantContext;
import br.com.megadashboard.service.DashboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
        return dashboardService.render(id, tenant);
    }

    @GetMapping(value = "/resumo", produces = MediaType.APPLICATION_JSON_VALUE)
    public DashboardResumoRenderResponse resumo(
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String status
    ) {
        String tenant = TenantContext.getTenant();
        return dashboardService.resumo(tenant, dataInicio, dataFim, categoria, status);
    }

}
