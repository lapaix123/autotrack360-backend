package rw.autotrack.autotrack360.shipment;

import lombok.Data;
import rw.autotrack.autotrack360.vehicle.VehicleDTO;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ShipmentDTO {
    private Long id;
    private String name;
    private String origin;
    private String destination;
    private ShipmentStatus status;
    private List<VehicleDTO> vehicles;

    public static ShipmentDTO from(Shipment s) {
        ShipmentDTO dto = new ShipmentDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setOrigin(s.getOrigin());
        dto.setDestination(s.getDestination());
        dto.setStatus(s.getStatus());
        dto.setVehicles(s.getVehicles().stream().map(VehicleDTO::from).collect(Collectors.toList()));
        return dto;
    }
}
