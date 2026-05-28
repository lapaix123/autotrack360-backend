package rw.autotrack.autotrack360.shipment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    public ResponseEntity<ShipmentDTO> create(@RequestBody ShipmentDTO dto) {
        return ResponseEntity.ok(shipmentService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ShipmentDTO>> getAll() {
        return ResponseEntity.ok(shipmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.findById(id));
    }

    @PostMapping("/{id}/vehicles/{vehicleId}")
    public ResponseEntity<ShipmentDTO> addVehicle(@PathVariable Long id, @PathVariable Long vehicleId) {
        return ResponseEntity.ok(shipmentService.addVehicle(id, vehicleId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ShipmentDTO> updateStatus(@PathVariable Long id, @RequestParam ShipmentStatus status) {
        return ResponseEntity.ok(shipmentService.updateStatus(id, status));
    }

    /**
     * SHIPPING_COMPANY: update GPS location of a shipment
     * PATCH /api/shipments/{id}/gps
     */
    @PatchMapping("/{id}/gps")
    public ResponseEntity<ShipmentDTO> updateGps(
            @PathVariable Long id,
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(shipmentService.updateGps(id, lat, lng, location));
    }

    /**
     * Public endpoint — no auth required. Customers use this to track their shipment.
     * GET /api/shipments/track/{trackingNumber}
     */
    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<ShipmentDTO> track(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(shipmentService.trackByNumber(trackingNumber));
    }
}
