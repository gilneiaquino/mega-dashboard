package br.com.megadashboard.api.dto;

import java.util.List;

public class DashboardDto {
    public Long id;
    public String nome;
    public String descricao;
    public Boolean ativo;

    public String tenantCodigo; // retorna, mas no POST você ignora o que vier

    public List<DashboardItemDto> itens;
}
