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

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "descricao", length = 200)
    private String descricao;

    // relacionamento com tenant se necessário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    // getters & setters
}
