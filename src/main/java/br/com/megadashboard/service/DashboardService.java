package br.com.megadashboard.service;

import br.com.megadashboard.api.mapper.DashboardMapper;
import br.com.megadashboard.controller.dto.dashboard.*;
import br.com.megadashboard.core.NotFoundException;
import br.com.megadashboard.model.Dashboard;
import br.com.megadashboard.model.DashboardItem;
import br.com.megadashboard.model.TipoDashboardItem;
import br.com.megadashboard.repository.DashboardItemRepository;
import br.com.megadashboard.repository.DashboardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final DashboardItemRepository dashboardItemRepository;

    public DashboardService(DashboardRepository dashboardRepository,
                            DashboardItemRepository dashboardItemRepository) {
        this.dashboardRepository = dashboardRepository;
        this.dashboardItemRepository = dashboardItemRepository;
    }

    @Transactional
    public DashboardResponse criar(DashboardRequest request, String tenantCodigo) {
        validarCriacao(request);

        Dashboard entity = DashboardMapper.toEntity(request, tenantCodigo);

        // OBS: o mapper já faz item.setDashboard(dashboard)
        Dashboard salvo = dashboardRepository.save(entity);

        return DashboardMapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public DashboardResponse buscarPorId(Long id, String tenantCodigo) {
        Dashboard d = dashboardRepository.findByIdAndTenantCodigo(id, tenantCodigo)
                .orElseThrow(() -> new NotFoundException("Dashboard não encontrado"));
        return DashboardMapper.toResponse(d);
    }

    @Transactional(readOnly = true)
    public Page<DashboardResumoResponse> listar(String tenantCodigo, String nome, Pageable pageable) {
        Page<Dashboard> page = (nome == null || nome.isBlank())
                ? dashboardRepository.findByTenantCodigo(tenantCodigo, pageable)
                : dashboardRepository.findByTenantCodigoAndNomeContainingIgnoreCase(tenantCodigo, nome.trim(), pageable);

        return page.map(DashboardMapper::toResumoResponse);
    }
    @Transactional(readOnly = true)
    public DashboardRenderResponse render(Long dashboardId, String tenantCodigo) {
        Dashboard dash = dashboardRepository.findByIdAndTenantCodigo(dashboardId, tenantCodigo)
                .orElseThrow(() -> new NotFoundException("Dashboard não encontrado"));

        List<DashboardItem> itens = dashboardItemRepository
                .findByDashboardIdAndAtivoTrueOrderByOrdemAsc(dash.getId());

        var cards = itens.stream()
                .map(item -> CardResponse.builder()
                        .id("item_" + item.getId())
                        .tipo(toCardTipo(item.getTipo()))   // ✅ AQUI
                        .titulo(item.getTitulo())
                        .ordem(item.getOrdem())
                        .colSpan(item.getColSpan())
                        .data(renderCardData(item, tenantCodigo))
                        .build())
                .toList();

        return DashboardRenderResponse.builder()
                .dashboardId(dash.getId())
                .titulo(dash.getNome())
                .cards(cards)
                .build();
    }

    private CardTipo toCardTipo(TipoDashboardItem tipo) {
        // se os nomes forem iguais (KPI, CHART_BAR...), isso resolve 100%
        return CardTipo.valueOf(tipo.name());
    }

    private Map<String, Object> renderCardData(DashboardItem item, String tenantCodigo) {
        CardTipo tipo = toCardTipo(item.getTipo());

        return switch (tipo) {
            case KPI -> Map.of(
                    "valor", 12345,
                    "variacaoPct", 2.3,
                    "meta", item.getMetaKpi()
            );
            case CHART_BAR -> Map.of(
                    "labels", List.of("Ativo", "Atraso", "Liquidado"),
                    "series", List.of(Map.of("name", "Qtd", "values", List.of(10, 5, 20)))
            );
        };
    }

    private void validarCriacao(DashboardRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Body obrigatório");
        }

        if (request.nome == null || request.nome.trim().isEmpty()) {
            throw new IllegalArgumentException("nome é obrigatório");
        }

        if (request.itens == null || request.itens.isEmpty()) {
            throw new IllegalArgumentException("itens é obrigatório");
        }

        request.itens.forEach(i -> {
            if (i == null) {
                throw new IllegalArgumentException("item do dashboard não pode ser nulo");
            }
            if (i.titulo == null || i.titulo.trim().isEmpty()) {
                throw new IllegalArgumentException("titulo do item é obrigatório");
            }
            if (i.tipo == null) {
                throw new IllegalArgumentException("tipo do item é obrigatório");
            }
            if (i.relatorioId == null) {
                throw new IllegalArgumentException("relatorioId do item é obrigatório");
            }
            if (i.colunaRotulo == null || i.colunaRotulo.trim().isEmpty()) {
                throw new IllegalArgumentException("colunaRotulo é obrigatória");
            }
            if (i.colunaValor == null || i.colunaValor.trim().isEmpty()) {
                throw new IllegalArgumentException("colunaValor é obrigatória");
            }
        });
    }
}
