package br.com.megadashboard.controller.dto.dashboard;

import java.util.List;

public class DashboardResumoRenderResponse {

    private List<PieItem> carteiraFisica;
    private List<PieItem> carteiraFinanceira;
    private List<BarItem> evolucao;

    public DashboardResumoRenderResponse() {}

    public DashboardResumoRenderResponse(List<PieItem> carteiraFisica, List<PieItem> carteiraFinanceira, List<BarItem> evolucao) {
        this.carteiraFisica = carteiraFisica;
        this.carteiraFinanceira = carteiraFinanceira;
        this.evolucao = evolucao;
    }

    public List<PieItem> getCarteiraFisica() { return carteiraFisica; }
    public void setCarteiraFisica(List<PieItem> carteiraFisica) { this.carteiraFisica = carteiraFisica; }

    public List<PieItem> getCarteiraFinanceira() { return carteiraFinanceira; }
    public void setCarteiraFinanceira(List<PieItem> carteiraFinanceira) { this.carteiraFinanceira = carteiraFinanceira; }

    public List<BarItem> getEvolucao() { return evolucao; }
    public void setEvolucao(List<BarItem> evolucao) { this.evolucao = evolucao; }

    public static class PieItem {
        private String label;
        private Number value;

        public PieItem() {}
        public PieItem(String label, Number value) { this.label = label; this.value = value; }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public Number getValue() { return value; }
        public void setValue(Number value) { this.value = value; }
    }

    public static class BarItem {
        private String label;
        private List<BarSerieItem> series;

        public BarItem() {}
        public BarItem(String label, List<BarSerieItem> series) { this.label = label; this.series = series; }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public List<BarSerieItem> getSeries() { return series; }
        public void setSeries(List<BarSerieItem> series) { this.series = series; }
    }

    public static class BarSerieItem {
        private String label;
        private Number value;

        public BarSerieItem() {}
        public BarSerieItem(String label, Number value) { this.label = label; this.value = value; }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public Number getValue() { return value; }
        public void setValue(Number value) { this.value = value; }
    }
}
