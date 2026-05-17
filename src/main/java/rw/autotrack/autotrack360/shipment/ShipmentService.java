package rw.autotrack.autotrack360.shipment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rw.autotrack.autotrack360.notification.EmailService;
import rw.autotrack.autotrack360.notification.SmsService;
import rw.autotrack.autotrack360.sales.Sale;
import rw.autotrack.autotrack360.sales.SaleRepository;
import rw.autotrack.autotrack360.vehicle.Vehicle;
import rw.autotrack.autotrack360.vehicle.VehicleService;
import rw.autotrack.autotrack360.vehicle.VehicleStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final VehicleService vehicleService;
    private final SmsService smsService;
    private final EmailService emailService;
    private final SaleRepository saleRepository;

    public ShipmentDTO create(ShipmentDTO dto) {
        String trackingNumber = "AT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Shipment shipment = Shipment.builder()
                .name(dto.getName())
                .origin(dto.getOrigin())
                .destination(dto.getDestination())
                .estimatedArrival(dto.getEstimatedArrival())
                .notes(dto.getNotes())
                .trackingNumber(trackingNumber)
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

    public ShipmentDTO trackByNumber(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber)
                .map(ShipmentDTO::from)
                .orElseThrow(() -> new RuntimeException("No shipment found with tracking number: " + trackingNumber));
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

        ShipmentDTO saved = ShipmentDTO.from(shipmentRepository.save(shipment));

        // Notify customers whose vehicles are in this shipment
        if (status == ShipmentStatus.ARRIVED) {
            shipment.getVehicles().forEach(vehicle -> {
                saleRepository.findAll().stream()
                    .filter(s -> s.getVehicle() != null && s.getVehicle().getId().equals(vehicle.getId()))
                    .findFirst()
                    .ifPresent(sale -> {
                        String email = sale.getCustomer().getEmail();
                        String phone = sale.getCustomer().getPhone();
                        String name = sale.getCustomer().getName();
                        emailService.sendVehicleArrived(email, name,
                            vehicle.getMake(), vehicle.getModel(),
                            shipment.getTrackingNumber(), shipment.getDestination());
                        smsService.sendShipmentUpdate(phone, name,
                            shipment.getTrackingNumber(), "ARRIVED", shipment.getDestination());
                    });
            });
        }

        return saved;
    }
}
