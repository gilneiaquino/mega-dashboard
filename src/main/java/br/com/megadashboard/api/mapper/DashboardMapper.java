package br.com.megadashboard.api.mapper;

import br.com.megadashboard.controller.dto.dashboard.DashboardItemRequest;
import br.com.megadashboard.controller.dto.dashboard.DashboardItemResponse;
import br.com.megadashboard.controller.dto.dashboard.DashboardRequest;
import br.com.megadashboard.controller.dto.dashboard.DashboardResponse;
import br.com.megadashboard.controller.dto.dashboard.DashboardResumoResponse;
import br.com.megadashboard.model.Dashboard;
import br.com.megadashboard.model.DashboardItem;

import java.util.ArrayList;
import java.util.List;

public class DashboardMapper {

    private DashboardMapper() {
        // util class
    }

    // --------- Request -> Entity ---------

    public static Dashboard toEntity(DashboardRequest request, String tenantCodigo) {
        Dashboard d = new Dashboard();
        d.setNome(request.nome);
        d.setDescricao(request.descricao);
        d.setAtivo(request.ativo != null ? request.ativo : Boolean.TRUE);
        d.setTenantCodigo(tenantCodigo);

        if (request.itens != null) {
            List<DashboardItem> itens = new ArrayList<>();
            for (DashboardItemRequest i : request.itens) {
                DashboardItem item = toEntity(i);
                item.setDashboard(d);
                itens.add(item);
            }
            d.setItens(itens);
        }

        return d;
    }

    public static DashboardItem toEntity(DashboardItemRequest request) {
        DashboardItem i = new DashboardItem();
        i.setTitulo(request.titulo);
        i.setTipo(request.tipo);
        i.setRelatorioId(request.relatorioId);
        i.setColunaRotulo(request.colunaRotulo);
        i.setColunaValor(request.colunaValor);
        i.setAgregacao(request.agregacao);
        i.setFiltroJson(request.filtroJson);
        i.setConfiguracaoVisualJson(request.configuracaoVisualJson);
        i.setOrdem(request.ordem);
        i.setMetaKpi(request.metaKpi);
        return i;
    }

    // --------- Entity -> Response ---------

    public static DashboardResponse toResponse(Dashboard d) {
        DashboardResponse r = new DashboardResponse();
        r.id = d.getId();
        r.nome = d.getNome();
        r.descricao = d.getDescricao();
        r.ativo = d.getAtivo();
        r.tenantCodigo = d.getTenantCodigo();

        if (d.getItens() != null) {
            r.itens = d.getItens().stream().map(DashboardMapper::toResponse).toList();
        }

        return r;
    }

    public static DashboardItemResponse toResponse(DashboardItem i) {
        DashboardItemResponse r = new DashboardItemResponse();
        r.id = i.getId();
        r.titulo = i.getTitulo();
        r.tipo = i.getTipo();
        r.relatorioId = i.getRelatorioId();
        r.colunaRotulo = i.getColunaRotulo();
        r.colunaValor = i.getColunaValor();
        r.agregacao = i.getAgregacao();
        r.filtroJson = i.getFiltroJson();
        r.configuracaoVisualJson = i.getConfiguracaoVisualJson();
        r.ordem = i.getOrdem();
        r.metaKpi = i.getMetaKpi();
        return r;
    }

    public static DashboardResumoResponse toResumoResponse(Dashboard d) {
        DashboardResumoResponse r = new DashboardResumoResponse();
        r.id = d.getId();
        r.nome = d.getNome();
        r.descricao = d.getDescricao();
        r.ativo = d.getAtivo();
        return r;
    }
}
