package br.com.megadashboard.controller.dto.relatorio;

import java.util.List;
import java.util.Map;

public record ExecutarRelatorioResponse(
        String tipo, // TABELA / KPI
        List<Map<String, Object>> linhas
) {}
