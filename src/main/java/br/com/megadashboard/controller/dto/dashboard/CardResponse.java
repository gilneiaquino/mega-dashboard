package br.com.megadashboard.controller.dto.dashboard;

import lombok.*;

import java.util.Map;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
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
