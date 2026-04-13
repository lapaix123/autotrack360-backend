package rw.autotrack.autotrack360.payment;

import jakarta.persistence.*;
import lombok.*;
import rw.autotrack.autotrack360.sales.Sale;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    private BigDecimal amount;
    private LocalDate date;
}
