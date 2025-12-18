package br.com.megadashboard.controller.dto.dashboard;

import br.com.megadashboard.model.TipoDashboardItem;

public class DashboardItemRequest {

    /** Título do gráfico / KPI */
    public String titulo;

    /** PIZZA | BARRA | LINHA | KPI */
    public TipoDashboardItem tipo;

    /** Fonte dos dados (id do relatório) */
    public Long relatorioId;

    /** Coluna usada como label */
    public String colunaRotulo;

    /** Coluna usada como valor */
    public String colunaValor;

    /** SUM | COUNT | AVG | MAX | MIN */
    public String agregacao;

    /** Filtros fixos do item (JSON) */
    public String filtroJson;

    /** Configuração visual (JSON) */
    public String configuracaoVisualJson;

    /** Ordem do item no dashboard */
    public Integer ordem;

    /** Meta para KPI */
    public Double metaKpi;
}
