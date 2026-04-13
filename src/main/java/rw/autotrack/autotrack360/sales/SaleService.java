package rw.autotrack.autotrack360.sales;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rw.autotrack.autotrack360.inventory.InventoryItem;
import rw.autotrack.autotrack360.inventory.InventoryService;
import rw.autotrack.autotrack360.inventory.InventoryStatus;
import rw.autotrack.autotrack360.vehicle.Vehicle;
import rw.autotrack.autotrack360.vehicle.VehicleService;
import rw.autotrack.autotrack360.vehicle.VehicleStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final VehicleService vehicleService;
    private final InventoryService inventoryService;

    public SaleDTOs.CustomerDTO createCustomer(SaleDTOs.CreateCustomerRequest req) {
        Customer customer = Customer.builder()
                .name(req.getName()).phone(req.getPhone()).email(req.getEmail())
                .build();
        return SaleDTOs.CustomerDTO.from(customerRepository.save(customer));
    }

    public List<SaleDTOs.CustomerDTO> findAllCustomers() {
        return customerRepository.findAll().stream()
                .map(SaleDTOs.CustomerDTO::from).collect(Collectors.toList());
    }

    public SaleDTOs.SaleDTO createSale(SaleDTOs.CreateSaleRequest req) {
        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Vehicle vehicle = vehicleService.getEntity(req.getVehicleId());

        Sale sale = Sale.builder()
                .customer(customer)
                .vehicle(vehicle)
                .totalAmount(req.getTotalAmount())
                .status(SaleStatus.PENDING)
                .build();

        // Reserve in inventory
        try {
            InventoryItem item = inventoryService.getByVehicleId(vehicle.getId());
            item.setStatus(InventoryStatus.RESERVED);
        } catch (Exception ignored) {}

        return SaleDTOs.SaleDTO.from(saleRepository.save(sale));
    }

    public SaleDTOs.SaleDTO completeSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        sale.setStatus(SaleStatus.COMPLETED);
        sale.getVehicle().setStatus(VehicleStatus.SOLD);

        try {
            InventoryItem item = inventoryService.getByVehicleId(sale.getVehicle().getId());
            item.setStatus(InventoryStatus.SOLD);
        } catch (Exception ignored) {}

        System.out.println("[NOTIFICATION] Sale #" + id + " completed for customer: " + sale.getCustomer().getName());
        return SaleDTOs.SaleDTO.from(saleRepository.save(sale));
    }

    public List<SaleDTOs.SaleDTO> findAll() {
        return saleRepository.findAll().stream().map(SaleDTOs.SaleDTO::from).collect(Collectors.toList());
    }

    public SaleDTOs.SaleDTO findById(Long id) {
        return saleRepository.findById(id).map(SaleDTOs.SaleDTO::from)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
    }
}
