package rw.autotrack.autotrack360.sales;

import lombok.Data;
import rw.autotrack.autotrack360.vehicle.VehicleDTO;

import java.math.BigDecimal;

public class SaleDTOs {

    @Data
    public static class CustomerDTO {
        private Long id;
        private String name;
        private String phone;
        private String email;

        public static CustomerDTO from(Customer c) {
            CustomerDTO dto = new CustomerDTO();
            dto.setId(c.getId());
            dto.setName(c.getName());
            dto.setPhone(c.getPhone());
            dto.setEmail(c.getEmail());
            return dto;
        }
    }

    @Data
    public static class CreateCustomerRequest {
        private String name;
        private String phone;
        private String email;
    }

    @Data
    public static class SaleDTO {
        private Long id;
        private CustomerDTO customer;
        private VehicleDTO vehicle;
        private BigDecimal totalAmount;
        private SaleStatus status;

        public static SaleDTO from(Sale s) {
            SaleDTO dto = new SaleDTO();
            dto.setId(s.getId());
            dto.setCustomer(CustomerDTO.from(s.getCustomer()));
            dto.setVehicle(VehicleDTO.from(s.getVehicle()));
            dto.setTotalAmount(s.getTotalAmount());
            dto.setStatus(s.getStatus());
            return dto;
        }
    }

    @Data
    public static class CreateSaleRequest {
        private Long customerId;
        private Long vehicleId;
        private BigDecimal totalAmount;
    }
}
