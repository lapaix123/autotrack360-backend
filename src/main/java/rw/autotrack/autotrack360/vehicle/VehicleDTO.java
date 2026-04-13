package rw.autotrack.autotrack360.vehicle;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VehicleDTO {
    private Long id;
    private String vin;
    private String make;
    private String model;
    private Integer year;
    private String color;
    private VehicleStatus status;
    private BigDecimal price;

    public static VehicleDTO from(Vehicle v) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(v.getId());
        dto.setVin(v.getVin());
        dto.setMake(v.getMake());
        dto.setModel(v.getModel());
        dto.setYear(v.getYear());
        dto.setColor(v.getColor());
        dto.setStatus(v.getStatus());
        dto.setPrice(v.getPrice());
        return dto;
    }
}
