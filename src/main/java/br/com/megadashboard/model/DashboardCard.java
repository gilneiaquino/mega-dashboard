package br.com.megadashboard.model;

import br.com.megadashboard.controller.dto.dashboard.CardTipo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DASHBOARD_CARD")
@Getter
@Setter
public class DashboardCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long dashboardId;

    @Column(nullable = false, length = 50)
    private String tenant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CardTipo tipo;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private Integer colSpan;

    // JSON com config do card (ex: métrica, query, etc.)
    @Column(columnDefinition = "TEXT")
    private String configJson;
}
