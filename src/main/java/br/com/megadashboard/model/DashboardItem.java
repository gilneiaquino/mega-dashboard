package br.com.megadashboard.model;

import br.com.megadashboard.model.TipoDashboardItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dashboard_item")
@Data
public class DashboardItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dashboard_id", nullable = false)
    private Dashboard dashboard;

    // VOLTA pro enum do model (o seu Mapper já usa ele)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoDashboardItem tipo;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false)
    private Integer ordem;

    @Column(name = "col_span", nullable = false)
    private Integer colSpan = 3;

    // ✅ Campos que o seu Mapper/Request já exigem
    @Column(name = "relatorio_id", nullable = false)
    private Long relatorioId;

    @Column(name = "coluna_rotulo", nullable = false, length = 120)
    private String colunaRotulo;

    @Column(name = "coluna_valor", nullable = false, length = 120)
    private String colunaValor;

    @Column(name = "agregacao", length = 30)
    private String agregacao;

    @Column(name = "filtro_json", columnDefinition = "TEXT")
    private String filtroJson;

    @Column(name = "config_visual_json", columnDefinition = "TEXT")
    private String configuracaoVisualJson;

    @Column(name = "meta_kpi")
    private Double metaKpi;

    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;
}
