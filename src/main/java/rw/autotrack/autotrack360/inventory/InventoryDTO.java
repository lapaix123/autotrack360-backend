package rw.autotrack.autotrack360.inventory;

import lombok.Data;
import rw.autotrack.autotrack360.vehicle.VehicleDTO;

@Data
public class InventoryDTO {
    private Long id;
    private VehicleDTO vehicle;
    private String location;
    private InventoryStatus status;

    public static InventoryDTO from(InventoryItem item) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(item.getId());
        dto.setVehicle(VehicleDTO.from(item.getVehicle()));
        dto.setLocation(item.getLocation());
        dto.setStatus(item.getStatus());
        return dto;
    }
}
