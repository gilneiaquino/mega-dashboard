package br.com.megadashboard.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "relatorio")
public class Relatorio {

    public enum TipoRelatorio {
        TABELA,
        KPI
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relatório pertence a um tenant (empresa)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Lob
    @Column(name = "sql_texto", nullable = false)
    private String sqlTexto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoRelatorio tipo;

    @OneToMany(mappedBy = "relatorio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParametroRelatorio> parametros = new ArrayList<>();

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Getters/Setters

    public Long getId() { return id; }
    public Tenant getTenant() { return tenant; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getSqlTexto() { return sqlTexto; }
    public TipoRelatorio getTipo() { return tipo; }
    public List<ParametroRelatorio> getParametros() { return parametros; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }

    public void setId(Long id) { this.id = id; }
    public void setTenant(Tenant tenant) { this.tenant = tenant; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setSqlTexto(String sqlTexto) { this.sqlTexto = sqlTexto; }
    public void setTipo(TipoRelatorio tipo) { this.tipo = tipo; }
    public void setParametros(List<ParametroRelatorio> parametros) { this.parametros = parametros; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    // helper para manter ambos lados:
    public void adicionarParametro(ParametroRelatorio p) {
        parametros.add(p);
        p.setRelatorio(this);
    }

    public void limparParametros() {
        for (ParametroRelatorio p : parametros) {
            p.setRelatorio(null);
        }
        parametros.clear();
    }
}
