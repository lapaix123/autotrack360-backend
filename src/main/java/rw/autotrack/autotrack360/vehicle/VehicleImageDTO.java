package rw.autotrack.autotrack360.vehicle;

import lombok.Data;

@Data
public class VehicleImageDTO {
    private Long id;
    private Long vehicleId;
    private String fileName;
    private String url;

    public static VehicleImageDTO from(VehicleImage img) {
        VehicleImageDTO dto = new VehicleImageDTO();
        dto.setId(img.getId());
        dto.setVehicleId(img.getVehicle().getId());
        dto.setFileName(img.getFileName());
        dto.setUrl(img.getUrl());
        return dto;
    }
}
