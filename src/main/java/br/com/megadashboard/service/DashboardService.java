package br.com.megadashboard.service;

import br.com.megadashboard.controller.dto.dashboard.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    public DashboardResponse montarDashboard(String tenant) {

        BigDecimal fator = switch (tenant) {
            case "empresa-x" -> BigDecimal.valueOf(1.0);
            case "empresa-y" -> BigDecimal.valueOf(1.5);
            default -> BigDecimal.valueOf(0.8);
        };

        List<PieItem> carteiraFisica = List.of(
                new PieItem("Adimplente", BigDecimal.valueOf(120).multiply(fator)),
                new PieItem("Inadimplente", BigDecimal.valueOf(30).multiply(fator))
        );

        List<PieItem> carteiraFinanceira = List.of(
                new PieItem("Adimplente", BigDecimal.valueOf(150_000).multiply(fator)),
                new PieItem("Inadimplente", BigDecimal.valueOf(25_000).multiply(fator))
        );

        List<BarItem> evolucao = List.of(
                new BarItem("Jan", List.of(
                        new BarSeriesItem("Contratos", BigDecimal.valueOf(10).multiply(fator)),
                        new BarSeriesItem("Valor (R$ mil)", BigDecimal.valueOf(80).multiply(fator))
                )),
                new BarItem("Fev", List.of(
                        new BarSeriesItem("Contratos", BigDecimal.valueOf(15).multiply(fator)),
                        new BarSeriesItem("Valor (R$ mil)", BigDecimal.valueOf(120).multiply(fator))
                )),
                new BarItem("Mar", List.of(
                        new BarSeriesItem("Contratos", BigDecimal.valueOf(20).multiply(fator)),
                        new BarSeriesItem("Valor (R$ mil)", BigDecimal.valueOf(160).multiply(fator))
                ))
        );

        return new DashboardResponse(carteiraFisica, carteiraFinanceira, evolucao);
    }
}
