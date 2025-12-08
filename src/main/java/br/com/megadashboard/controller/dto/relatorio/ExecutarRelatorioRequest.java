package br.com.megadashboard.controller.dto.relatorio;

import java.util.Map;

public record ExecutarRelatorioRequest(
        Map<String, Object> parametros
) {}
