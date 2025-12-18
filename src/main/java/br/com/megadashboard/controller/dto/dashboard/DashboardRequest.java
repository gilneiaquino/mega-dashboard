package br.com.megadashboard.controller.dto.dashboard;

import java.util.List;

public class DashboardRequest {
    public String nome;
    public String descricao;
    public Boolean ativo;
    public List<DashboardItemRequest> itens;
}
