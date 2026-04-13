package rw.autotrack.autotrack360.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rw.autotrack.autotrack360.sales.Sale;
import rw.autotrack.autotrack360.sales.SaleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SaleRepository saleRepository;

    public PaymentDTO record(PaymentDTO.CreatePaymentRequest req) {
        Sale sale = saleRepository.findById(req.getSaleId())
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        Payment payment = Payment.builder()
                .sale(sale)
                .amount(req.getAmount())
                .date(LocalDate.now())
                .build();
        System.out.println("[NOTIFICATION] Payment of " + req.getAmount() + " recorded for sale #" + req.getSaleId());
        return PaymentDTO.from(paymentRepository.save(payment));
    }

    public List<PaymentDTO> findBySale(Long saleId) {
        return paymentRepository.findBySaleId(saleId).stream()
                .map(PaymentDTO::from).collect(Collectors.toList());
    }

    public List<PaymentDTO> findAll() {
        return paymentRepository.findAll().stream().map(PaymentDTO::from).collect(Collectors.toList());
    }
}
