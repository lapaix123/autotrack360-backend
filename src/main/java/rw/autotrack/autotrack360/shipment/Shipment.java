package rw.autotrack.autotrack360.shipment;

import jakarta.persistence.*;
import lombok.*;
import rw.autotrack.autotrack360.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shipments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Shipment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String origin;
    private String destination;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    @ManyToMany
    @JoinTable(name = "shipment_vehicles",
            joinColumns = @JoinColumn(name = "shipment_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    @Builder.Default
    private List<Vehicle> vehicles = new ArrayList<>();
}
