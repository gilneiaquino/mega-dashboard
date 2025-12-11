package br.com.megadashboard.controller.dto.dashboard;

import br.com.megadashboard.model.TipoDashboardItem;

public class DashboardItemDto {
    private Long id;                  // null na criação
    private String titulo;
    private TipoDashboardItem tipo;

    private Long relatorioId;
    private String colunaRotulo;
    private String colunaValor;
    private String agregacao;        // "SUM", "COUNT", "AVG" etc.

    private String filtroJson;       // opcional
    private String configuracaoVisualJson;
    private Integer ordem;
    private Double metaKpi;
}
