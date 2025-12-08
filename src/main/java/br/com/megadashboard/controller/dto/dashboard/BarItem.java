package br.com.megadashboard.controller.dto.dashboard;

import java.util.List;

public record BarItem(
        String label,
        List<BarSeriesItem> series
) {}
