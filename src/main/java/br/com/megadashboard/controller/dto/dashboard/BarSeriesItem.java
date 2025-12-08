package br.com.megadashboard.controller.dto.dashboard;

import java.math.BigDecimal;

public record BarSeriesItem(
        String label,
        BigDecimal value
) {}

