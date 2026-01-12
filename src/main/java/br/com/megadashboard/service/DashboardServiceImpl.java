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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;
    private final DashboardItemRepository dashboardItemRepository;

    private final NamedParameterJdbcTemplate jdbc;

    public DashboardServiceImpl(DashboardRepository dashboardRepository,
                                DashboardItemRepository dashboardItemRepository,
                                NamedParameterJdbcTemplate jdbc) {
        this.dashboardRepository = dashboardRepository;
        this.dashboardItemRepository = dashboardItemRepository;
        this.jdbc = jdbc;
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
        if (tipo == null) return null;

        return switch (tipo) {
            case KPI -> CardTipo.KPI;
            case BARRA -> CardTipo.CHART_BAR;
            case LINHA -> CardTipo.CHART_LINE;
            case PIZZA -> CardTipo.CHART_PIE;
        };
    }

    private Map<String, Object> renderCardData(DashboardItem item, String tenantCodigo) {
        CardTipo tipo = toCardTipo(item.getTipo());

        return switch (tipo) {
            case KPI -> Map.of("valor", 12345, "variacaoPct", 2.3);

            case CHART_BAR -> Map.of(
                    "labels", List.of("Ativo", "Atraso", "Liquidado"),
                    "series", List.of(Map.of("name", "Qtd", "values", List.of(10, 5, 20)))
            );

            case CHART_LINE, CHART_PIE -> Map.of(); // Sprint 4: placeholder
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

    @Override
    public DashboardResumoRenderResponse resumo(String tenantCodigo,
                                                String dataInicio,
                                                String dataFim,
                                                String categoria,
                                                String status) {

        Long tenantId = buscarTenantId(tenantCodigo);

        var params = new MapSqlParameterSource();
        params.addValue("tenantId", tenantId);
        params.addValue("dataInicio", dataInicio); // yyyy-MM-dd (recomendado)
        params.addValue("dataFim", dataFim);
        params.addValue("categoria", categoria);
        params.addValue("status", status);

        // 1) Carteira Física (COUNT por status)
        // Ajuste: o.status / o.categoria / o.data_operacao conforme seu schema
        String sqlFisica = """
                    SELECT 
                      COALESCE(o.status, 'SEM_STATUS') AS label,
                      COUNT(*) AS value
                    FROM operacao o
                    WHERE o.tenant_id = :tenantId
                      AND (:dataInicio IS NULL OR o.data_operacao >= :dataInicio)
                      AND (:dataFim IS NULL OR o.data_operacao <= :dataFim)
                      AND (:categoria IS NULL OR o.categoria = :categoria)
                      AND (:status IS NULL OR :status = 'TODOS' OR o.status = :status)
                    GROUP BY o.status
                    ORDER BY value DESC
                """;

        var carteiraFisica = jdbc.query(sqlFisica, params, (rs, i) ->
                new DashboardResumoRenderResponse.PieItem(
                        rs.getString("label"),
                        rs.getLong("value")
                )
        );

        // 2) Carteira Financeira (SUM valor por status) – exemplo
        // Se você não tiver status/categoria, pode agrupar por outra coluna.
        String sqlFinanceira = """
                    SELECT 
                      COALESCE(o.status, 'SEM_STATUS') AS label,
                      COALESCE(SUM(o.valor), 0) AS value
                    FROM operacao o
                    WHERE o.tenant_id = :tenantId
                      AND (:dataInicio IS NULL OR o.data_operacao >= :dataInicio)
                      AND (:dataFim IS NULL OR o.data_operacao <= :dataFim)
                      AND (:categoria IS NULL OR o.categoria = :categoria)
                      AND (:status IS NULL OR :status = 'TODOS' OR o.status = :status)
                    GROUP BY o.status
                    ORDER BY value DESC
                """;

        var carteiraFinanceira = jdbc.query(sqlFinanceira, params, (rs, i) ->
                new DashboardResumoRenderResponse.PieItem(
                        rs.getString("label"),
                        rs.getBigDecimal("value") // Number ok
                )
        );

        // 3) Evolução mensal (COUNT + SUM no mesmo mês)
        // MySQL: DATE_FORMAT(data_operacao, '%Y-%m') dá o "label"
        String sqlEvolucao = """
                    SELECT
                      DATE_FORMAT(o.data_operacao, '%Y-%m') AS mes,
                      COUNT(*) AS qtd,
                      COALESCE(SUM(o.valor), 0) AS total
                    FROM operacao o
                    WHERE o.tenant_id = :tenantId
                      AND (:dataInicio IS NULL OR o.data_operacao >= :dataInicio)
                      AND (:dataFim IS NULL OR o.data_operacao <= :dataFim)
                      AND (:categoria IS NULL OR o.categoria = :categoria)
                      AND (:status IS NULL OR :status = 'TODOS' OR o.status = :status)
                    GROUP BY DATE_FORMAT(o.data_operacao, '%Y-%m')
                    ORDER BY mes ASC
                """;

        var evolucao = jdbc.query(sqlEvolucao, params, (rs, i) -> {
            String mes = rs.getString("mes");
            long qtd = rs.getLong("qtd");
            var total = rs.getBigDecimal("total");

            return new DashboardResumoRenderResponse.BarItem(
                    mes,
                    List.of(
                            new DashboardResumoRenderResponse.BarSerieItem("Operações", qtd),
                            new DashboardResumoRenderResponse.BarSerieItem("Valor", total)
                    )
            );
        });

        return new DashboardResumoRenderResponse(carteiraFisica, carteiraFinanceira, evolucao);
    }

    private Long buscarTenantId(String tenantCodigo) {
        String sql = "SELECT id FROM tenant WHERE codigo = :codigo";
        var params = new MapSqlParameterSource("codigo", tenantCodigo);

        return jdbc.query(sql, params, rs -> {
            if (rs.next()) return rs.getLong("id");
            throw new IllegalArgumentException("Tenant não encontrado: " + tenantCodigo);
        });
    }

}
