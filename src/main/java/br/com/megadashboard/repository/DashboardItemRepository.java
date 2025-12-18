package br.com.megadashboard.repository;

import br.com.megadashboard.model.DashboardItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardItemRepository extends JpaRepository<DashboardItem, Long> {
}
