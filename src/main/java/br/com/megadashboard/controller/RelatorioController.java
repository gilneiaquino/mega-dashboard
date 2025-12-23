package br.com.megadashboard.controller;

import br.com.megadashboard.controller.dto.relatorio.*;
import br.com.megadashboard.security.TenantContext;
import br.com.megadashboard.service.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @PostMapping
    public ResponseEntity<RelatorioResponse> criar(@RequestBody RelatorioRequest request) {
        RelatorioResponse resp = relatorioService.criar(request);
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<RelatorioResponse>> listar() {
        return ResponseEntity.ok(relatorioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RelatorioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(relatorioService.buscarPorId(id));
    }

    @PostMapping("/{id}/executar")
    public ResponseEntity<ExecutarRelatorioResponse> executar(
            @PathVariable Long id,
            @RequestBody ExecutarRelatorioRequest request
    ) {
        String tenant = TenantContext.getTenant();
        return ResponseEntity.ok(relatorioService.executar(id, request));
    }

}
