package br.com.megadashboard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dashboard_item")
@Getter
@Setter
public class DashboardItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dashboard pai
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id", nullable = false)
    private Dashboard dashboard;

    // título do gráfico ou KPI
    @Column(nullable = false, length = 150)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoDashboardItem tipo;

    // fonte: id do relatório (da tabela de relatórios que vc criou na Sprint 2)
    @Column(name = "relatorio_id", nullable = false)
    private Long relatorioId;

    // coluna que será usada como label (ex.: "mes", "agente", "pa")
    @Column(name = "coluna_rotulo", nullable = false, length = 100)
    private String colunaRotulo;

    // coluna que será usada como valor (ex.: "valorTotal", "quantidade")
    @Column(name = "coluna_valor", nullable = false, length = 100)
    private String colunaValor;

    // opcional: operação de agregação (SUM, COUNT, AVG…)
    @Column(name = "agregacao", length = 20)
    private String agregacao;

    // JSON com filtros pré-definidos para esse item (ex.: { "linhaCredito": 123, "pa": 10 })
    @Lob
    @Column(name = "filtro_json")
    private String filtroJson;

    // JSON de configurações visuais (cores, legenda, etc.) – o front interpreta
    @Lob
    @Column(name = "configuracao_visual_json")
    private String configuracaoVisualJson;

    // ordem do item no dashboard
    @Column(name = "ordem")
    private Integer ordem;

    // para KPI: valor alvo/meta (para comparar com o valor real)
    @Column(name = "meta_kpi")
    private Double metaKpi;

    // getters / setters
}
