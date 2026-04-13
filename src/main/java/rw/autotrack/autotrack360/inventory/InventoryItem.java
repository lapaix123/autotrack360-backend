package rw.autotrack.autotrack360.inventory;

import jakarta.persistence.*;
import lombok.*;
import rw.autotrack.autotrack360.vehicle.Vehicle;

@Entity
@Table(name = "inventory_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InventoryItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "vehicle_id", unique = true)
    private Vehicle vehicle;

    private String location;

    @Enumerated(EnumType.STRING)
    private InventoryStatus status;
}
