package rw.autotrack.autotrack360.vehicle;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 17)
    private String chassisNumber;

    private String make;
    private String model;
    private Integer year;
    private String color;
    private String description;
    private String engineType;
    private String transmission;
    private Integer mileage;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    private BigDecimal price;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<VehicleImage> images = new ArrayList<>();
}
