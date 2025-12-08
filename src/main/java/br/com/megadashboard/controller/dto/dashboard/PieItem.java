package br.com.megadashboard.controller.dto.dashboard;

import java.math.BigDecimal;

public record PieItem(
        String label,
        BigDecimal value
) {}
