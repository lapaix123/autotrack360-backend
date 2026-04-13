package rw.autotrack.autotrack360.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rw.autotrack.autotrack360.inventory.InventoryRepository;
import rw.autotrack.autotrack360.inventory.InventoryStatus;
import rw.autotrack.autotrack360.sales.SaleRepository;
import rw.autotrack.autotrack360.sales.SaleStatus;
import rw.autotrack.autotrack360.vehicle.VehicleRepository;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final VehicleRepository vehicleRepository;
    private final SaleRepository saleRepository;
    private final InventoryRepository inventoryRepository;

    public DashboardDTO getSummary() {
        return new DashboardDTO(
                vehicleRepository.count(),
                saleRepository.count(),
                inventoryRepository.countByStatus(InventoryStatus.AVAILABLE),
                saleRepository.countByStatus(SaleStatus.COMPLETED),
                saleRepository.countByStatus(SaleStatus.PENDING)
        );
    }
}
