package rw.autotrack.autotrack360.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDTO> record(@RequestBody PaymentDTO.CreatePaymentRequest req) {
        return ResponseEntity.ok(paymentService.record(req));
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAll() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping("/sale/{saleId}")
    public ResponseEntity<List<PaymentDTO>> getBySale(@PathVariable Long saleId) {
        return ResponseEntity.ok(paymentService.findBySale(saleId));
    }
}
