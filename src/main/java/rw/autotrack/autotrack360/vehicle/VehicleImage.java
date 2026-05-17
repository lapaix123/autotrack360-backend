package rw.autotrack.autotrack360.vehicle;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicle_images")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    private String fileName;
    private String filePath;
    private String url;
}
