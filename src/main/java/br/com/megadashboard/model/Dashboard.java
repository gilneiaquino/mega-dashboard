package br.com.megadashboard.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dashboard")
@Data
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nome visível do dashboard
    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 500)
    private String descricao;

    // multi-tenant
    @Column(name = "tenant_codigo", nullable = false, length = 50)
    private String tenantCodigo;

    @Column(name = "criado_por", length = 100)
    private String criadoPor;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = Boolean.TRUE;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @OneToMany(
            mappedBy = "dashboard",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("ordem ASC")
    private List<DashboardItem> itens = new ArrayList<>();

    @PrePersist
    void prePersist() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = dataCriacao;
    }

    @PreUpdate
    void preUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    // getters / setters
}
