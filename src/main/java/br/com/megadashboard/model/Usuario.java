package br.com.megadashboard.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false, unique = true, length = 100)
    private String login;

    @Column(name = "senha", nullable = false)
    private String senha; // cifrada com BCrypt

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false, length = 20)
    private Perfil perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    // ============
    // Construtores
    // ============

    public Usuario() {
    }

    public Usuario(Long id, String login, String senha, String nome,
                   Perfil perfil, Tenant tenant, Boolean ativo) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.perfil = perfil;
        this.tenant = tenant;
        this.ativo = ativo;
    }

    // ========
    // Getters
    // ========

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    // ========
    // Setters
    // ========

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
