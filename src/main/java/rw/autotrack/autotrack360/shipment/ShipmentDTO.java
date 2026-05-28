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
    private String trackingNumber;
    private String estimatedArrival;
    private String notes;
    private ShipmentStatus status;
    private Double currentLatitude;
    private Double currentLongitude;
    private String currentLocation;
    private String lastGpsUpdate;
    private String shippingCompanyName;
    private List<VehicleDTO> vehicles;

    public static ShipmentDTO from(Shipment s) {
        ShipmentDTO dto = new ShipmentDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setOrigin(s.getOrigin());
        dto.setDestination(s.getDestination());
        dto.setTrackingNumber(s.getTrackingNumber());
        dto.setEstimatedArrival(s.getEstimatedArrival());
        dto.setNotes(s.getNotes());
        dto.setStatus(s.getStatus());
        dto.setCurrentLatitude(s.getCurrentLatitude());
        dto.setCurrentLongitude(s.getCurrentLongitude());
        dto.setCurrentLocation(s.getCurrentLocation());
        dto.setLastGpsUpdate(s.getLastGpsUpdate() != null ? s.getLastGpsUpdate().toString() : null);
        dto.setShippingCompanyName(s.getShippingCompanyName());
        dto.setVehicles(s.getVehicles().stream().map(VehicleDTO::from).collect(Collectors.toList()));
        return dto;
    }
}
