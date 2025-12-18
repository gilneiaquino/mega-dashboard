package br.com.megadashboard.controller.dto.dashboard;

import br.com.megadashboard.model.TipoDashboardItem;

public class DashboardItemResponse {

    public Long id;

    /** Título exibido no card / gráfico */
    public String titulo;

    /** PIZZA | BARRA | LINHA | KPI */
    public TipoDashboardItem tipo;

    /** Fonte dos dados */
    public Long relatorioId;

    /** Coluna usada como label (eixo X, legenda, etc.) */
    public String colunaRotulo;

    /** Coluna usada como valor */
    public String colunaValor;

    /** SUM | COUNT | AVG | MAX | MIN (opcional) */
    public String agregacao;

    /** Filtros fixos do item (JSON) */
    public String filtroJson;

    /** Configurações visuais (JSON) */
    public String configuracaoVisualJson;

    /** Ordem de exibição no dashboard */
    public Integer ordem;

    /** Meta usada apenas para KPI */
    public Double metaKpi;
}
