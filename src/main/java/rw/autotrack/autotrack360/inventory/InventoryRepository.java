package rw.autotrack.autotrack360.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByVehicleId(Long vehicleId);
    long countByStatus(InventoryStatus status);
}
