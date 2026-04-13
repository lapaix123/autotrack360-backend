package rw.autotrack.autotrack360.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardDTO {
    private long totalVehicles;
    private long totalSales;
    private long availableInventory;
    private long completedSales;
    private long pendingSales;
}
