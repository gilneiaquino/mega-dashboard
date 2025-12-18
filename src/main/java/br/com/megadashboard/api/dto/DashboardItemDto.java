package br.com.megadashboard.api.dto;

import br.com.megadashboard.model.TipoDashboardItem;

public class DashboardItemDto {
    public Long id;
    public String titulo;
    public TipoDashboardItem tipo;

    public Long relatorioId;
    public String colunaRotulo;
    public String colunaValor;
    public String agregacao;

    public String filtroJson;
    public String configuracaoVisualJson;

    public Integer ordem;
    public Double metaKpi;
}
