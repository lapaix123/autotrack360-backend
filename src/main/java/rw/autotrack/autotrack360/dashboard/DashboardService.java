package rw.autotrack.autotrack360.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rw.autotrack.autotrack360.inventory.InventoryRepository;
import rw.autotrack.autotrack360.inventory.InventoryStatus;
import rw.autotrack.autotrack360.payment.Payment;
import rw.autotrack.autotrack360.payment.PaymentRepository;
import rw.autotrack.autotrack360.sales.Sale;
import rw.autotrack.autotrack360.sales.SaleRepository;
import rw.autotrack.autotrack360.sales.SaleStatus;
import rw.autotrack.autotrack360.shipment.Shipment;
import rw.autotrack.autotrack360.shipment.ShipmentRepository;
import rw.autotrack.autotrack360.shipment.ShipmentStatus;
import rw.autotrack.autotrack360.vehicle.Vehicle;
import rw.autotrack.autotrack360.vehicle.VehicleRepository;

import java.math.BigDecimal;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final VehicleRepository vehicleRepository;
    private final SaleRepository saleRepository;
    private final InventoryRepository inventoryRepository;
    private final ShipmentRepository shipmentRepository;
    private final PaymentRepository paymentRepository;

    public SalesDashboardDTO getSalesSummary() {
        List<Sale> sales = saleRepository.findAll();
        List<Payment> payments = paymentRepository.findAll();
        long totalCustomers = sales.stream().map(s -> s.getCustomer().getId()).distinct().count();

        BigDecimal totalRevenue = payments.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal pendingRevenue = sales.stream()
            .filter(s -> s.getStatus() == rw.autotrack.autotrack360.sales.SaleStatus.PENDING)
            .map(Sale::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Long> ordersByStatus = new LinkedHashMap<>();
        for (Sale s : sales) ordersByStatus.merge(s.getStatus().name(), 1L, Long::sum);

        // Monthly revenue
        Map<String, BigDecimal> monthMap = new LinkedHashMap<>();
        java.time.LocalDate now = java.time.LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            java.time.LocalDate m = now.minusMonths(i);
            String key = m.getMonth().getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH) + " " + m.getYear();
            monthMap.put(key, BigDecimal.ZERO);
        }
        for (Payment p : payments) {
            if (p.getDate() != null) {
                String key = p.getDate().getMonth().getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH) + " " + p.getDate().getYear();
                monthMap.merge(key, p.getAmount(), BigDecimal::add);
            }
        }
        List<DashboardDTO.MonthlyRevenue> monthlyRevenue = monthMap.entrySet().stream()
            .map(e -> new DashboardDTO.MonthlyRevenue(e.getKey(), e.getValue())).collect(Collectors.toList());

        // Recent orders
        List<SalesDashboardDTO.RecentOrder> recentOrders = sales.stream()
            .sorted(Comparator.comparing(Sale::getId).reversed()).limit(5)
            .map(s -> new SalesDashboardDTO.RecentOrder(
                s.getId(), s.getCustomer().getName(), s.getCustomer().getEmail(),
                s.getVehicle().getMake() + " " + s.getVehicle().getModel(),
                s.getTotalAmount(), s.getStatus().name()
            )).collect(Collectors.toList());

        // Top customers
        Map<Long, List<Sale>> byCust = sales.stream().collect(Collectors.groupingBy(s -> s.getCustomer().getId()));
        List<SalesDashboardDTO.TopCustomer> topCustomers = byCust.entrySet().stream()
            .map(e -> {
                List<Sale> cs = e.getValue();
                BigDecimal spent = cs.stream().map(Sale::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                return new SalesDashboardDTO.TopCustomer(
                    cs.get(0).getCustomer().getName(), cs.get(0).getCustomer().getEmail(),
                    cs.size(), spent);
            })
            .sorted(Comparator.comparing(SalesDashboardDTO.TopCustomer::getTotalSpent).reversed())
            .limit(5).collect(Collectors.toList());

        SalesDashboardDTO dto = new SalesDashboardDTO();
        dto.setTotalOrders(sales.size());
        dto.setPendingOrders(saleRepository.countByStatus(SaleStatus.PENDING));
        dto.setCompletedOrders(saleRepository.countByStatus(SaleStatus.COMPLETED));
        dto.setTotalCustomers(totalCustomers);
        dto.setTotalRevenue(totalRevenue);
        dto.setPendingRevenue(pendingRevenue);
        dto.setOrdersByStatus(ordersByStatus);
        dto.setMonthlyRevenue(monthlyRevenue);
        dto.setRecentOrders(recentOrders);
        dto.setTopCustomers(topCustomers);
        return dto;
    }

    public LogisticsDashboardDTO getLogisticsSummary() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Shipment> shipments = shipmentRepository.findAll();

        Map<String, Long> vehiclesByStatus = new LinkedHashMap<>();
        for (Vehicle v : vehicles) vehiclesByStatus.merge(v.getStatus().name(), 1L, Long::sum);

        Map<String, Long> shipmentsByStatus = new LinkedHashMap<>();
        for (Shipment s : shipments) shipmentsByStatus.merge(s.getStatus().name(), 1L, Long::sum);

        List<LogisticsDashboardDTO.ActiveShipment> activeList = shipments.stream()
            .filter(s -> s.getStatus() == ShipmentStatus.SHIPPED || s.getStatus() == ShipmentStatus.CREATED)
            .sorted(Comparator.comparing(Shipment::getId).reversed())
            .map(s -> new LogisticsDashboardDTO.ActiveShipment(
                s.getId(), s.getName(), s.getTrackingNumber(),
                s.getOrigin(), s.getDestination(), s.getStatus().name(),
                s.getVehicles().size(), s.getEstimatedArrival()
            )).collect(Collectors.toList());

        List<LogisticsDashboardDTO.RecentVehicle> recentVehicles = vehicles.stream()
            .sorted(Comparator.comparing(Vehicle::getId).reversed()).limit(6)
            .map(v -> {
                String imgUrl = v.getImages() != null && !v.getImages().isEmpty()
                    ? v.getImages().get(0).getUrl() : null;
                return new LogisticsDashboardDTO.RecentVehicle(
                    v.getId(), v.getMake(), v.getModel(), v.getYear(), v.getVin(),
                    v.getStatus().name(), imgUrl);
            }).collect(Collectors.toList());

        LogisticsDashboardDTO dto = new LogisticsDashboardDTO();
        dto.setTotalVehicles(vehicles.size());
        dto.setVehiclesInTransit(vehiclesByStatus.getOrDefault("IN_TRANSIT", 0L));
        dto.setVehiclesArrived(vehiclesByStatus.getOrDefault("ARRIVED", 0L));
        dto.setVehiclesAvailable(vehiclesByStatus.getOrDefault("AVAILABLE", 0L));
        dto.setTotalShipments(shipments.size());
        dto.setActiveShipments(shipments.stream().filter(s -> s.getStatus() == ShipmentStatus.SHIPPED).count());
        dto.setArrivedShipments(shipments.stream().filter(s -> s.getStatus() == ShipmentStatus.ARRIVED).count());
        dto.setTotalInventory(inventoryRepository.count());
        dto.setVehiclesByStatus(vehiclesByStatus);
        dto.setShipmentsByStatus(shipmentsByStatus);
        dto.setActiveShipmentList(activeList);
        dto.setRecentVehicles(recentVehicles);
        return dto;
    }

    public DashboardDTO getSummary() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Sale> sales = saleRepository.findAll();
        List<Shipment> shipments = shipmentRepository.findAll();
        List<Payment> payments = paymentRepository.findAll();

        // Vehicle status breakdown
        Map<String, Long> vehiclesByStatus = new LinkedHashMap<>();
        for (Vehicle v : vehicles) vehiclesByStatus.merge(v.getStatus().name(), 1L, Long::sum);

        // Shipment status breakdown
        Map<String, Long> shipmentsByStatus = new LinkedHashMap<>();
        for (Shipment s : shipments) shipmentsByStatus.merge(s.getStatus().name(), 1L, Long::sum);

        // Total revenue from payments
        BigDecimal totalRevenue = payments.stream()
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Monthly revenue (last 6 months from payments)
        Map<String, BigDecimal> monthMap = new LinkedHashMap<>();
        // Pre-fill last 6 months with zero
        java.time.LocalDate now = java.time.LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            java.time.LocalDate m = now.minusMonths(i);
            String key = m.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + m.getYear();
            monthMap.put(key, BigDecimal.ZERO);
        }
        for (Payment p : payments) {
            if (p.getDate() != null) {
                String key = p.getDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + p.getDate().getYear();
                monthMap.merge(key, p.getAmount(), BigDecimal::add);
            }
        }
        List<DashboardDTO.MonthlyRevenue> monthlyRevenue = monthMap.entrySet().stream()
            .map(e -> new DashboardDTO.MonthlyRevenue(e.getKey(), e.getValue()))
            .collect(Collectors.toList());

        // Recent sales (last 5)
        List<DashboardDTO.RecentSale> recentSales = sales.stream()
            .sorted(Comparator.comparing(Sale::getId).reversed())
            .limit(5)
            .map(s -> new DashboardDTO.RecentSale(
                s.getId(),
                s.getCustomer().getName(),
                s.getVehicle().getMake() + " " + s.getVehicle().getModel(),
                s.getTotalAmount(),
                s.getStatus().name()
            )).collect(Collectors.toList());

        // Recent shipments (last 5)
        List<DashboardDTO.RecentShipment> recentShipments = shipments.stream()
            .sorted(Comparator.comparing(Shipment::getId).reversed())
            .limit(5)
            .map(s -> new DashboardDTO.RecentShipment(
                s.getId(),
                s.getName(),
                s.getTrackingNumber(),
                s.getOrigin(),
                s.getDestination(),
                s.getStatus().name(),
                s.getVehicles().size()
            )).collect(Collectors.toList());

        long activeShipments = shipments.stream()
            .filter(s -> s.getStatus() == ShipmentStatus.SHIPPED).count();

        DashboardDTO dto = new DashboardDTO();
        dto.setTotalVehicles(vehicles.size());
        dto.setTotalSales(sales.size());
        dto.setAvailableInventory(inventoryRepository.countByStatus(InventoryStatus.AVAILABLE));
        dto.setCompletedSales(saleRepository.countByStatus(SaleStatus.COMPLETED));
        dto.setPendingSales(saleRepository.countByStatus(SaleStatus.PENDING));
        dto.setTotalShipments(shipments.size());
        dto.setActiveShipments(activeShipments);
        dto.setTotalRevenue(totalRevenue);
        dto.setVehiclesByStatus(vehiclesByStatus);
        dto.setShipmentsByStatus(shipmentsByStatus);
        dto.setMonthlyRevenue(monthlyRevenue);
        dto.setRecentSales(recentSales);
        dto.setRecentShipments(recentShipments);
        return dto;
    }
}
