package br.com.megadashboard.service;

import br.com.megadashboard.api.mapper.DashboardMapper;
import br.com.megadashboard.controller.dto.dashboard.*;
import br.com.megadashboard.core.NotFoundException;
import br.com.megadashboard.model.Dashboard;
import br.com.megadashboard.model.DashboardCard;
import br.com.megadashboard.repository.DashboardCardRepository;
import br.com.megadashboard.repository.DashboardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final DashboardRepository repo;
    private final DashboardCardRepository dashboardCardRepository;

    public DashboardService(DashboardRepository repo, DashboardCardRepository dashboardCardRepository) {
        this.dashboardCardRepository = dashboardCardRepository;
        this.repo = repo;
    }

    @Transactional
    public DashboardResponse criar(DashboardRequest request, String tenantCodigo) {
        validarCriacao(request);

        Dashboard entity = DashboardMapper.toEntity(request, tenantCodigo);

        // OBS: o mapper já faz item.setDashboard(dashboard)
        Dashboard salvo = repo.save(entity);

        return DashboardMapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public DashboardResponse buscarPorId(Long id, String tenantCodigo) {
        Dashboard d = repo.findByIdAndTenantCodigo(id, tenantCodigo)
                .orElseThrow(() -> new NotFoundException("Dashboard não encontrado"));
        return DashboardMapper.toResponse(d);
    }

    @Transactional(readOnly = true)
    public Page<DashboardResumoResponse> listar(String tenantCodigo, String nome, Pageable pageable) {
        Page<Dashboard> page = (nome == null || nome.isBlank())
                ? repo.findByTenantCodigo(tenantCodigo, pageable)
                : repo.findByTenantCodigoAndNomeContainingIgnoreCase(tenantCodigo, nome.trim(), pageable);

        return page.map(DashboardMapper::toResumoResponse);
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

    public DashboardRenderResponse render(Long dashboardId, String tenant) {
        var cards = dashboardCardRepository
                .findByDashboardIdAndTenantOrderByOrdemAsc(dashboardId, tenant);

        var responses = cards.stream()
                .map(card -> CardResponse.builder()
                        .id("card_" + card.getId())
                        .tipo(card.getTipo())
                        .titulo(card.getTitulo())
                        .ordem(card.getOrdem())
                        .colSpan(card.getColSpan())
                        .data(renderCardData(card, tenant)) // aqui entra o “motor”
                        .build()
                ).toList();

        return DashboardRenderResponse.builder()
                .dashboardId(dashboardId)
                .titulo("Visão Geral (" + tenant + ")")
                .cards(responses)
                .build();
    }

    private Map<String, Object> renderCardData(DashboardCard card, String tenant) {
        return switch (card.getTipo()) {
            case KPI -> Map.of("valor", 12345, "variacaoPct", 2.3);
            case CHART_BAR -> Map.of(
                    "labels", List.of("Ativo", "Atraso", "Liquidado"),
                    "series", List.of(Map.of("name", "Qtd", "values", List.of(10, 5, 20)))
            );
        };
    }

}
