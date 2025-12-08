package br.com.megadashboard.controller;

import br.com.megadashboard.controller.dto.dashboard.DashboardResponse;
import br.com.megadashboard.security.TenantContext;
import br.com.megadashboard.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse obterDashboard() {
        String tenant = TenantContext.getTenant();
        return dashboardService.montarDashboard(tenant);
    }
}
