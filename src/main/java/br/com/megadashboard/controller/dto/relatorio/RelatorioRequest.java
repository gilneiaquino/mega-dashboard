package br.com.megadashboard.controller.dto.relatorio;

import java.util.List;

public record RelatorioRequest(
        String nome,
        String descricao,
        String sqlTexto,
        String tipo, // TABELA ou KPI
        List<ParametroRelatorioDTO> parametros
) {}
