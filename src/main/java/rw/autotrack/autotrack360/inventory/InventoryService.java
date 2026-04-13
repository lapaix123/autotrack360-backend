package rw.autotrack.autotrack360.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rw.autotrack.autotrack360.vehicle.Vehicle;
import rw.autotrack.autotrack360.vehicle.VehicleService;
import rw.autotrack.autotrack360.vehicle.VehicleStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final VehicleService vehicleService;

    public InventoryDTO addToInventory(Long vehicleId, String location) {
        Vehicle vehicle = vehicleService.getEntity(vehicleId);
        InventoryItem item = InventoryItem.builder()
                .vehicle(vehicle)
                .location(location)
                .status(InventoryStatus.AVAILABLE)
                .build();
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        return InventoryDTO.from(inventoryRepository.save(item));
    }

    public List<InventoryDTO> findAll() {
        return inventoryRepository.findAll().stream().map(InventoryDTO::from).collect(Collectors.toList());
    }

    public InventoryItem getByVehicleId(Long vehicleId) {
        return inventoryRepository.findByVehicleId(vehicleId)
                .orElseThrow(() -> new RuntimeException("Inventory item not found for vehicle"));
    }
}
