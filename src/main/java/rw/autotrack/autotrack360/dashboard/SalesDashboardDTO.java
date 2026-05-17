package rw.autotrack.autotrack360.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data @AllArgsConstructor @NoArgsConstructor
public class SalesDashboardDTO {
    private long totalOrders;
    private long pendingOrders;
    private long completedOrders;
    private long totalCustomers;
    private BigDecimal totalRevenue;
    private BigDecimal pendingRevenue;

    private Map<String, Long> ordersByStatus;
    private List<DashboardDTO.MonthlyRevenue> monthlyRevenue;
    private List<RecentOrder> recentOrders;
    private List<TopCustomer> topCustomers;

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class RecentOrder {
        private Long id;
        private String customerName;
        private String customerEmail;
        private String vehicle;
        private BigDecimal amount;
        private String status;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class TopCustomer {
        private String name;
        private String email;
        private long orderCount;
        private BigDecimal totalSpent;
    }
}
