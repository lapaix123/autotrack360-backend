package rw.autotrack.autotrack360.vehicle;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class VehicleDTO {
    private Long id;
    private String chassisNumber;
    private String make;
    private String model;
    private Integer year;
    private String color;
    private String description;
    private String engineType;
    private String transmission;
    private Integer mileage;
    private VehicleStatus status;
    private BigDecimal price;
    private List<VehicleImageDTO> images;

    public static VehicleDTO from(Vehicle v) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(v.getId());
        dto.setChassisNumber(v.getChassisNumber());
        dto.setMake(v.getMake());
        dto.setModel(v.getModel());
        dto.setYear(v.getYear());
        dto.setColor(v.getColor());
        dto.setDescription(v.getDescription());
        dto.setEngineType(v.getEngineType());
        dto.setTransmission(v.getTransmission());
        dto.setMileage(v.getMileage());
        dto.setStatus(v.getStatus());
        dto.setPrice(v.getPrice());
        if (v.getImages() != null) {
            dto.setImages(v.getImages().stream().map(VehicleImageDTO::from).collect(Collectors.toList()));
        }
        return dto;
    }
}
