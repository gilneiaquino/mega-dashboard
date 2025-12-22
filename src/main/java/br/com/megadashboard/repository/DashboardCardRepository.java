package br.com.megadashboard.repository;

import br.com.megadashboard.model.DashboardCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DashboardCardRepository extends JpaRepository<DashboardCard, Long> {
    List<DashboardCard> findByDashboardIdAndTenantOrderByOrdemAsc(Long dashboardId, String tenant);
}
