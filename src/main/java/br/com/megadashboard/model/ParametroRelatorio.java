package br.com.megadashboard.model;

import jakarta.persistence.*;

@Entity
@Table(name = "parametro_relatorio")
public class ParametroRelatorio {

    public enum TipoParametro {
        STRING,
        INTEGER,
        DECIMAL,
        DATE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relatorio_id", nullable = false)
    private Relatorio relatorio;

    @Column(nullable = false, length = 50)
    private String nome; // ex: dataInicial, agencia

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoParametro tipo;

    @Column(name = "obrigatorio", nullable = false)
    private boolean obrigatorio;

    // Getters/Setters

    public Long getId() { return id; }
    public Relatorio getRelatorio() { return relatorio; }
    public String getNome() { return nome; }
    public TipoParametro getTipo() { return tipo; }
    public boolean isObrigatorio() { return obrigatorio; }

    public void setId(Long id) { this.id = id; }
    public void setRelatorio(Relatorio relatorio) { this.relatorio = relatorio; }
    public void setNome(String nome) { this.nome = nome; }
    public void setTipo(TipoParametro tipo) { this.tipo = tipo; }
    public void setObrigatorio(boolean obrigatorio) { this.obrigatorio = obrigatorio; }
}
