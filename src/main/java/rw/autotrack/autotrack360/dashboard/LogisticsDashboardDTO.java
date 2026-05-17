package rw.autotrack.autotrack360.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data @AllArgsConstructor @NoArgsConstructor
public class LogisticsDashboardDTO {
    private long totalVehicles;
    private long vehiclesInTransit;
    private long vehiclesArrived;
    private long vehiclesAvailable;
    private long totalShipments;
    private long activeShipments;
    private long arrivedShipments;
    private long totalInventory;

    private Map<String, Long> vehiclesByStatus;
    private Map<String, Long> shipmentsByStatus;
    private List<ActiveShipment> activeShipmentList;
    private List<RecentVehicle> recentVehicles;

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class ActiveShipment {
        private Long id;
        private String name;
        private String trackingNumber;
        private String origin;
        private String destination;
        private String status;
        private int vehicleCount;
        private String estimatedArrival;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class RecentVehicle {
        private Long id;
        private String make;
        private String model;
        private Integer year;
        private String vin;
        private String status;
        private String imageUrl;
    }
}
