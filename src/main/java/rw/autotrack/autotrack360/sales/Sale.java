package rw.autotrack.autotrack360.sales;

import jakarta.persistence.*;
import lombok.*;
import rw.autotrack.autotrack360.vehicle.Vehicle;

import java.math.BigDecimal;

@Entity
@Table(name = "sales")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Sale {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private SaleStatus status;
}
