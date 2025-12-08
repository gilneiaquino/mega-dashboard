package br.com.megadashboard.controller.dto.relatorio;

public record ParametroRelatorioDTO(
        String nome,
        String tipo,       // STRING, INTEGER, DECIMAL, DATE
        boolean obrigatorio
) {}
