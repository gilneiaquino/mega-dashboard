package br.com.megadashboard.service;

import br.com.megadashboard.controller.dto.relatorio.*;
import br.com.megadashboard.model.ParametroRelatorio;
import br.com.megadashboard.model.Relatorio;
import br.com.megadashboard.model.Tenant;
import br.com.megadashboard.repository.RelatorioRepository;
import br.com.megadashboard.repository.TenantRepository;
import br.com.megadashboard.security.TenantContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;
    private final TenantRepository tenantRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RelatorioService(RelatorioRepository relatorioRepository,
                            TenantRepository tenantRepository,
                            NamedParameterJdbcTemplate jdbcTemplate) {
        this.relatorioRepository = relatorioRepository;
        this.tenantRepository = tenantRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    private String getTenantCodigo() {
        String tenant = TenantContext.getTenant();
        if (tenant == null) {
            throw new IllegalStateException("Tenant não informado");
        }
        return tenant;
    }

    // ========= Cadastro / Consulta =========

    @Transactional
    public RelatorioResponse criar(RelatorioRequest request) {
        String tenantCodigo = getTenantCodigo();

        Tenant tenant = tenantRepository.findByCodigoAndAtivoTrue(tenantCodigo)
                .orElseThrow(() -> new IllegalStateException("Tenant inválido"));

        validarSql(request.sqlTexto());

        Relatorio rel = new Relatorio();
        rel.setTenant(tenant);
        rel.setNome(request.nome());
        rel.setDescricao(request.descricao());
        rel.setSqlTexto(request.sqlTexto());
        rel.setTipo(Relatorio.TipoRelatorio.valueOf(request.tipo()));

        rel.limparParametros();
        if (request.parametros() != null) {
            for (ParametroRelatorioDTO p : request.parametros()) {
                ParametroRelatorio param = new ParametroRelatorio();
                param.setNome(p.nome());
                param.setTipo(ParametroRelatorio.TipoParametro.valueOf(p.tipo()));
                param.setObrigatorio(p.obrigatorio());
                rel.adicionarParametro(param);
            }
        }

        relatorioRepository.save(rel);
        return toResponse(rel);
    }

    @Transactional(readOnly = true)
    public List<RelatorioResponse> listar() {
        String tenantCodigo = getTenantCodigo();
        return relatorioRepository.findAllByTenant_CodigoOrderByNomeAsc(tenantCodigo)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RelatorioResponse buscarPorId(Long id) {
        String tenantCodigo = getTenantCodigo();
        Relatorio rel = relatorioRepository.findByIdAndTenant_Codigo(id, tenantCodigo)
                .orElseThrow(() -> new NoSuchElementException("Relatório não encontrado"));

        return toResponse(rel);
    }

    private RelatorioResponse toResponse(Relatorio rel) {
        List<ParametroRelatorioDTO> params = rel.getParametros().stream()
                .map(p -> new ParametroRelatorioDTO(
                        p.getNome(),
                        p.getTipo().name(),
                        p.isObrigatorio()
                ))
                .toList();

        return new RelatorioResponse(
                rel.getId(),
                rel.getNome(),
                rel.getDescricao(),
                rel.getTipo().name(),
                params
        );
    }

    // ========= Execução =========

    @Transactional(readOnly = true)
    public ExecutarRelatorioResponse executar(Long id, ExecutarRelatorioRequest request) {
        String tenantCodigo = getTenantCodigo();

        Relatorio rel = relatorioRepository.findByIdAndTenant_Codigo(id, tenantCodigo)
                .orElseThrow(() -> new NoSuchElementException("Relatório não encontrado"));

        validarSql(rel.getSqlTexto());

        Map<String, Object> paramsReq = request.parametros() != null
                ? request.parametros()
                : Collections.emptyMap();

        // valida parâmetros obrigatórios
        Set<String> obrigatorios = rel.getParametros().stream()
                .filter(ParametroRelatorio::isObrigatorio)
                .map(ParametroRelatorio::getNome)
                .collect(Collectors.toSet());

        for (String nomeParam : obrigatorios) {
            if (!paramsReq.containsKey(nomeParam)) {
                throw new IllegalArgumentException("Parâmetro obrigatório não informado: " + nomeParam);
            }
        }

        // Apenas parâmetros conhecidos
        Set<String> nomesPermitidos = rel.getParametros().stream()
                .map(ParametroRelatorio::getNome)
                .collect(Collectors.toSet());

        Map<String, Object> paramsFinal = new HashMap<>();
        for (Map.Entry<String, Object> e : paramsReq.entrySet()) {
            if (!nomesPermitidos.contains(e.getKey())) {
                throw new IllegalArgumentException("Parâmetro não reconhecido: " + e.getKey());
            }
            paramsFinal.put(e.getKey(), e.getValue());
        }

        List<Map<String, Object>> linhas;
        try {
            linhas = jdbcTemplate.queryForList(rel.getSqlTexto(), paramsFinal);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Erro ao executar relatório: " + e.getMessage(), e);
        }

        return new ExecutarRelatorioResponse(rel.getTipo().name(), linhas);
    }

    // ========= Validação básica do SQL =========

    private void validarSql(String sql) {
        if (sql == null || sql.isBlank()) {
            throw new IllegalArgumentException("SQL do relatório não pode ser vazio");
        }

        String upper = sql.trim().toUpperCase(Locale.ROOT);

        if (!upper.startsWith("SELECT")) {
            throw new IllegalArgumentException("Apenas consultas SELECT são permitidas");
        }

        List<String> proibidos = List.of(" DELETE ", " UPDATE ", " INSERT ", " DROP ", " ALTER ", " TRUNCATE ");
        for (String palavra : proibidos) {
            if (upper.contains(palavra)) {
                throw new IllegalArgumentException("SQL contém operação proibida: " + palavra.trim());
            }
        }
    }
}
