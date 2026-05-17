package rw.autotrack.autotrack360.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    // Summary cards
    private long totalVehicles;
    private long totalSales;
    private long availableInventory;
    private long completedSales;
    private long pendingSales;
    private long totalShipments;
    private long activeShipments;
    private BigDecimal totalRevenue;

    // Charts
    private Map<String, Long> vehiclesByStatus;   // for donut chart
    private Map<String, Long> shipmentsByStatus;  // for bar chart
    private List<MonthlyRevenue> monthlyRevenue;  // for line/bar chart

    // Recent activity
    private List<RecentSale> recentSales;
    private List<RecentShipment> recentShipments;

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class MonthlyRevenue {
        private String month;
        private BigDecimal amount;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class RecentSale {
        private Long id;
        private String customerName;
        private String vehicle;
        private BigDecimal amount;
        private String status;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class RecentShipment {
        private Long id;
        private String name;
        private String trackingNumber;
        private String origin;
        private String destination;
        private String status;
        private int vehicleCount;
    }
}
