package br.com.megadashboard.controller.dto.dashboard;

import java.util.List;

public record DashboardResponse(
        List<PieItem> carteiraFisica,
        List<PieItem> carteiraFinanceira,
        List<BarItem> evolucao
) {}
