package br.com.megadashboard.controller.dto.relatorio;

import java.util.List;

public record RelatorioResponse(
        Long id,
        String nome,
        String descricao,
        String tipo,
        List<ParametroRelatorioDTO> parametros
) {}
