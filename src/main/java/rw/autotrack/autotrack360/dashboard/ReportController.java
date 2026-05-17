package rw.autotrack.autotrack360.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.autotrack.autotrack360.payment.PaymentRepository;
import rw.autotrack.autotrack360.sales.Sale;
import rw.autotrack.autotrack360.sales.SaleRepository;
import rw.autotrack.autotrack360.shipment.Shipment;
import rw.autotrack.autotrack360.shipment.ShipmentRepository;
import rw.autotrack.autotrack360.vehicle.Vehicle;
import rw.autotrack.autotrack360.vehicle.VehicleRepository;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final SaleRepository saleRepository;
    private final VehicleRepository vehicleRepository;
    private final ShipmentRepository shipmentRepository;
    private final PaymentRepository paymentRepository;

    // ── Sales ────────────────────────────────────────────────

    @GetMapping("/sales")
    public ResponseEntity<SalesReportResponse> salesReport() {
        List<Sale> sales = saleRepository.findAll();
        BigDecimal totalRevenue = sales.stream()
            .map(Sale::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        long completed = sales.stream().filter(s -> s.getStatus().name().equals("COMPLETED")).count();
        long pending = sales.stream().filter(s -> s.getStatus().name().equals("PENDING")).count();

        List<SaleRow> rows = sales.stream().map(s -> new SaleRow(
            s.getId(),
            s.getCustomer().getName(),
            s.getCustomer().getEmail() != null ? s.getCustomer().getEmail() : "",
            s.getVehicle().getMake() + " " + s.getVehicle().getModel(),
            s.getVehicle().getVin(),
            s.getTotalAmount(),
            s.getStatus().name()
        )).toList();

        return ResponseEntity.ok(new SalesReportResponse(sales.size(), completed, pending, totalRevenue, rows));
    }

    @GetMapping("/sales/export")
    public ResponseEntity<byte[]> exportSalesCsv() {
        List<Sale> sales = saleRepository.findAll();
        StringBuilder csv = new StringBuilder("ID,Customer,Email,Vehicle,VIN,Amount,Status\n");
        for (Sale s : sales) {
            csv.append(s.getId()).append(",")
               .append(escape(s.getCustomer().getName())).append(",")
               .append(escape(s.getCustomer().getEmail())).append(",")
               .append(escape(s.getVehicle().getMake() + " " + s.getVehicle().getModel())).append(",")
               .append(escape(s.getVehicle().getVin())).append(",")
               .append(s.getTotalAmount()).append(",")
               .append(s.getStatus().name()).append("\n");
        }
        return csvResponse(csv.toString(), "sales-report.csv");
    }

    // ── Vehicles ─────────────────────────────────────────────

    @GetMapping("/vehicles")
    public ResponseEntity<VehiclesReportResponse> vehiclesReport() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (Vehicle v : vehicles) byStatus.merge(v.getStatus().name(), 1L, Long::sum);

        List<VehicleRow> rows = vehicles.stream().map(v -> new VehicleRow(
            v.getId(), v.getVin(), v.getMake(), v.getModel(),
            v.getYear(), v.getColor(), v.getStatus().name(), v.getPrice()
        )).toList();

        return ResponseEntity.ok(new VehiclesReportResponse(vehicles.size(), byStatus, rows));
    }

    @GetMapping("/vehicles/export")
    public ResponseEntity<byte[]> exportVehiclesCsv() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        StringBuilder csv = new StringBuilder("ID,VIN,Make,Model,Year,Color,Status,Price\n");
        for (Vehicle v : vehicles) {
            csv.append(v.getId()).append(",")
               .append(escape(v.getVin())).append(",")
               .append(escape(v.getMake())).append(",")
               .append(escape(v.getModel())).append(",")
               .append(v.getYear()).append(",")
               .append(escape(v.getColor())).append(",")
               .append(v.getStatus().name()).append(",")
               .append(v.getPrice()).append("\n");
        }
        return csvResponse(csv.toString(), "vehicles-report.csv");
    }

    // ── Shipments ────────────────────────────────────────────

    @GetMapping("/shipments")
    public ResponseEntity<ShipmentsReportResponse> shipmentsReport() {
        List<Shipment> shipments = shipmentRepository.findAll();
        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (Shipment s : shipments) byStatus.merge(s.getStatus().name(), 1L, Long::sum);

        List<ShipmentRow> rows = shipments.stream().map(s -> new ShipmentRow(
            s.getId(), s.getName(), s.getTrackingNumber(),
            s.getOrigin(), s.getDestination(), s.getStatus().name(), s.getVehicles().size()
        )).toList();

        return ResponseEntity.ok(new ShipmentsReportResponse(shipments.size(), byStatus, rows));
    }

    @GetMapping("/shipments/export")
    public ResponseEntity<byte[]> exportShipmentsCsv() {
        List<Shipment> shipments = shipmentRepository.findAll();
        StringBuilder csv = new StringBuilder("ID,Name,Tracking,Origin,Destination,Status,Vehicles\n");
        for (Shipment s : shipments) {
            csv.append(s.getId()).append(",")
               .append(escape(s.getName())).append(",")
               .append(escape(s.getTrackingNumber())).append(",")
               .append(escape(s.getOrigin())).append(",")
               .append(escape(s.getDestination())).append(",")
               .append(s.getStatus().name()).append(",")
               .append(s.getVehicles().size()).append("\n");
        }
        return csvResponse(csv.toString(), "shipments-report.csv");
    }

    // ── Helpers ──────────────────────────────────────────────

    private ResponseEntity<byte[]> csvResponse(String csv, String filename) {
        byte[] bytes = csv.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(bytes);
    }

    private String escape(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"") || val.contains("\n"))
            return "\"" + val.replace("\"", "\"\"") + "\"";
        return val;
    }

    // ── Response types ───────────────────────────────────────

    @Data @AllArgsConstructor
    public static class SalesReportResponse {
        private long totalSales;
        private long completedSales;
        private long pendingSales;
        private BigDecimal totalRevenue;
        private List<SaleRow> sales;
    }

    @Data @AllArgsConstructor
    public static class SaleRow {
        private Long id;
        private String customer;
        private String customerEmail;
        private String vehicle;
        private String vin;
        private BigDecimal amount;
        private String status;
    }

    @Data @AllArgsConstructor
    public static class VehiclesReportResponse {
        private long totalVehicles;
        private Map<String, Long> byStatus;
        private List<VehicleRow> vehicles;
    }

    @Data @AllArgsConstructor
    public static class VehicleRow {
        private Long id;
        private String vin;
        private String make;
        private String model;
        private Integer year;
        private String color;
        private String status;
        private BigDecimal price;
    }

    @Data @AllArgsConstructor
    public static class ShipmentsReportResponse {
        private long totalShipments;
        private Map<String, Long> byStatus;
        private List<ShipmentRow> shipments;
    }

    @Data @AllArgsConstructor
    public static class ShipmentRow {
        private Long id;
        private String name;
        private String trackingNumber;
        private String origin;
        private String destination;
        private String status;
        private int vehicleCount;
    }
}
