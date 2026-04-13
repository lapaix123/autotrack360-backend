package rw.autotrack.autotrack360.payment;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentDTO {
    private Long id;
    private Long saleId;
    private BigDecimal amount;
    private LocalDate date;

    public static PaymentDTO from(Payment p) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(p.getId());
        dto.setSaleId(p.getSale().getId());
        dto.setAmount(p.getAmount());
        dto.setDate(p.getDate());
        return dto;
    }

    @Data
    public static class CreatePaymentRequest {
        private Long saleId;
        private BigDecimal amount;
    }
}
