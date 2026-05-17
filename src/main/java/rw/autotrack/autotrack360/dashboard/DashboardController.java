package rw.autotrack.autotrack360.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/sales")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<SalesDashboardDTO> getSalesSummary() {
        return ResponseEntity.ok(dashboardService.getSalesSummary());
    }

    @GetMapping("/logistics")
    @PreAuthorize("hasAnyRole('ADMIN','LOGISTICS')")
    public ResponseEntity<LogisticsDashboardDTO> getLogisticsSummary() {
        return ResponseEntity.ok(dashboardService.getLogisticsSummary());
    }
}
