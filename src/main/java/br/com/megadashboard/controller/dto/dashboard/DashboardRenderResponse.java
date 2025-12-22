package br.com.megadashboard.controller.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardRenderResponse {
    private Long dashboardId;
    private String titulo;
    private List<CardResponse> cards;
}
