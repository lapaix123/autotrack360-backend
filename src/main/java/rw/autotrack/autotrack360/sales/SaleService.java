package rw.autotrack.autotrack360.sales;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rw.autotrack.autotrack360.inventory.InventoryItem;
import rw.autotrack.autotrack360.inventory.InventoryService;
import rw.autotrack.autotrack360.inventory.InventoryStatus;
import rw.autotrack.autotrack360.notification.EmailService;
import rw.autotrack.autotrack360.notification.SmsService;
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
    private final SmsService smsService;
    private final EmailService emailService;

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

        try {
            InventoryItem item = inventoryService.getByVehicleId(vehicle.getId());
            item.setStatus(InventoryStatus.RESERVED);
        } catch (Exception ignored) {}

        Sale saved = saleRepository.save(sale);

        // Notify customer via SMS + Email
        if (customer.getPhone() != null && !customer.getPhone().isBlank()) {
            smsService.send(customer.getPhone(),
                    "Dear " + customer.getName() + ", your order for " + vehicle.getMake()
                    + " " + vehicle.getModel() + " has been received. Sale ID: #" + saved.getId()
                    + ". AutoTrack360");
        }
        emailService.sendOrderConfirmation(
            customer.getEmail(), customer.getName(),
            vehicle.getMake(), vehicle.getModel(),
            String.valueOf(saved.getId()),
            req.getTotalAmount().toPlainString()
        );

        return SaleDTOs.SaleDTO.from(saved);
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

        Sale saved = saleRepository.save(sale);

        // SMS confirmation to customer
        smsService.sendSaleConfirmation(
                sale.getCustomer().getPhone(),
                sale.getCustomer().getName(),
                sale.getVehicle().getMake(),
                sale.getVehicle().getModel(),
                String.valueOf(saved.getId())
        );

        return SaleDTOs.SaleDTO.from(saved);
    }

    public List<SaleDTOs.SaleDTO> findAll() {
        return saleRepository.findAll().stream().map(SaleDTOs.SaleDTO::from).collect(Collectors.toList());
    }

    public SaleDTOs.SaleDTO findById(Long id) {
        return saleRepository.findById(id).map(SaleDTOs.SaleDTO::from)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
    }
}
