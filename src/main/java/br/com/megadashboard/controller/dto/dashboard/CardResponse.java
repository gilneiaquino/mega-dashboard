package br.com.megadashboard.controller.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {
    private String id;
    private CardTipo tipo;
    private String titulo;
    private Integer ordem;
    private Integer colSpan;

    /**
     * Payload flexível por tipo de card (KPI, CHART, TABLE, etc.)
     */
    private Map<String, Object> data;
}
