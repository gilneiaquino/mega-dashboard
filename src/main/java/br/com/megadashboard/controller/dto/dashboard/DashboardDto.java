package br.com.megadashboard.controller.dto.dashboard;

import br.com.megadashboard.controller.dto.dashboard.DashboardItemDto;

import java.util.List;

public class DashboardDto {
    private Long id;
    private String nome;
    private String descricao;
    private Boolean ativo;

    // preenchido automaticamente do token, mas pode ser retornado
    private String tenantCodigo;

    private List<DashboardItemDto> itens;
}
