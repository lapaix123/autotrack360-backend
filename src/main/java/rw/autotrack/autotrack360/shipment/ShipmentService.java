package rw.autotrack.autotrack360.shipment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rw.autotrack.autotrack360.vehicle.Vehicle;
import rw.autotrack.autotrack360.vehicle.VehicleService;
import rw.autotrack.autotrack360.vehicle.VehicleStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final VehicleService vehicleService;

    public ShipmentDTO create(ShipmentDTO dto) {
        Shipment shipment = Shipment.builder()
                .name(dto.getName())
                .origin(dto.getOrigin())
                .destination(dto.getDestination())
                .status(ShipmentStatus.CREATED)
                .build();
        return ShipmentDTO.from(shipmentRepository.save(shipment));
    }

    public List<ShipmentDTO> findAll() {
        return shipmentRepository.findAll().stream().map(ShipmentDTO::from).collect(Collectors.toList());
    }

    public ShipmentDTO findById(Long id) {
        return shipmentRepository.findById(id).map(ShipmentDTO::from)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
    }

    public ShipmentDTO addVehicle(Long shipmentId, Long vehicleId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
        Vehicle vehicle = vehicleService.getEntity(vehicleId);
        vehicle.setStatus(VehicleStatus.IN_TRANSIT);
        shipment.getVehicles().add(vehicle);
        return ShipmentDTO.from(shipmentRepository.save(shipment));
    }

    public ShipmentDTO updateStatus(Long id, ShipmentStatus status) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
        shipment.setStatus(status);
        if (status == ShipmentStatus.ARRIVED) {
            shipment.getVehicles().forEach(v -> v.setStatus(VehicleStatus.ARRIVED));
        }
        return ShipmentDTO.from(shipmentRepository.save(shipment));
    }
}
