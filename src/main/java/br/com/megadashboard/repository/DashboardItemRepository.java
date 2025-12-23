package br.com.megadashboard.repository;

import br.com.megadashboard.model.DashboardItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DashboardItemRepository extends JpaRepository<DashboardItem, Long> {

    List<DashboardItem> findByDashboardIdAndAtivoTrueOrderByOrdemAsc(Long dashboardId);

}
