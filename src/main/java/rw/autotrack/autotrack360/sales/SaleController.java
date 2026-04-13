package rw.autotrack.autotrack360.sales;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping("/customers")
    public ResponseEntity<SaleDTOs.CustomerDTO> createCustomer(@RequestBody SaleDTOs.CreateCustomerRequest req) {
        return ResponseEntity.ok(saleService.createCustomer(req));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<SaleDTOs.CustomerDTO>> getCustomers() {
        return ResponseEntity.ok(saleService.findAllCustomers());
    }

    @PostMapping
    public ResponseEntity<SaleDTOs.SaleDTO> createSale(@RequestBody SaleDTOs.CreateSaleRequest req) {
        return ResponseEntity.ok(saleService.createSale(req));
    }

    @GetMapping
    public ResponseEntity<List<SaleDTOs.SaleDTO>> getAll() {
        return ResponseEntity.ok(saleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTOs.SaleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.findById(id));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<SaleDTOs.SaleDTO> complete(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.completeSale(id));
    }
}
