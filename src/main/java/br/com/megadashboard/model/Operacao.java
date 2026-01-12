package br.com.megadashboard.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "operacao")
@Data
public class Operacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_operacao", nullable = false)
    private LocalDate dataOperacao;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "descricao", length = 200)
    private String descricao;

    // 🔹 NOVO: Status da operação (ex: EM_DIA, ATRASADO, LIQUIDADO)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private StatusOperacao status;

    // 🔹 NOVO: Categoria (ex: CREDITO, DEBITO, TARIFA)
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", length = 30, nullable = false)
    private CategoriaOperacao categoria;

    // relacionamento com tenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
}
