package rw.autotrack.autotrack360.vehicle;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleImageRepository vehicleImageRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.base-url}")
    private String baseUrl;

    public VehicleDTO create(VehicleDTO dto) {
        if (dto.getChassisNumber() == null || dto.getChassisNumber().trim().length() != 17) {
            throw new RuntimeException("Chassis number must be exactly 17 characters");
        }
        Vehicle vehicle = Vehicle.builder()
                .chassisNumber(dto.getChassisNumber().toUpperCase().trim())
                .make(dto.getMake()).model(dto.getModel())
                .year(dto.getYear()).color(dto.getColor())
                .description(dto.getDescription())
                .engineType(dto.getEngineType())
                .transmission(dto.getTransmission())
                .mileage(dto.getMileage())
                .status(dto.getStatus() != null ? dto.getStatus() : VehicleStatus.IMPORTED)
                .price(dto.getPrice())
                .build();
        return VehicleDTO.from(vehicleRepository.save(vehicle));
    }

    public List<VehicleDTO> findAll() {
        return vehicleRepository.findAll().stream().map(VehicleDTO::from).collect(Collectors.toList());
    }

    public VehicleDTO findById(Long id) {
        return vehicleRepository.findById(id).map(VehicleDTO::from)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    public VehicleDTO update(Long id, VehicleDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        vehicle.setMake(dto.getMake());
        vehicle.setModel(dto.getModel());
        vehicle.setYear(dto.getYear());
        vehicle.setColor(dto.getColor());
        vehicle.setDescription(dto.getDescription());
        vehicle.setEngineType(dto.getEngineType());
        vehicle.setTransmission(dto.getTransmission());
        vehicle.setMileage(dto.getMileage());
        vehicle.setPrice(dto.getPrice());
        return VehicleDTO.from(vehicleRepository.save(vehicle));
    }

    public VehicleDTO changeStatus(Long id, VehicleStatus status) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        vehicle.setStatus(status);
        return VehicleDTO.from(vehicleRepository.save(vehicle));
    }

    public VehicleImageDTO uploadImage(Long vehicleId, MultipartFile file) throws IOException {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Path uploadPath = Paths.get(uploadDir, "vehicles");
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        VehicleImage image = VehicleImage.builder()
                .vehicle(vehicle)
                .fileName(file.getOriginalFilename())
                .filePath(uploadPath.resolve(filename).toString())
                .url(baseUrl + "/api/vehicles/" + vehicleId + "/images/" + filename)
                .build();

        return VehicleImageDTO.from(vehicleImageRepository.save(image));
    }

    public List<VehicleImageDTO> getImages(Long vehicleId) {
        return vehicleImageRepository.findByVehicleId(vehicleId)
                .stream().map(VehicleImageDTO::from).collect(Collectors.toList());
    }

    public org.springframework.core.io.Resource serveImage(Long vehicleId, String filename) throws IOException {
        Path filePath = Paths.get(uploadDir, "vehicles", filename);
        org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());
        if (!resource.exists()) throw new RuntimeException("Image not found");
        return resource;
    }

    public void deleteImage(Long imageId) {
        VehicleImage image = vehicleImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        try { Files.deleteIfExists(Paths.get(image.getFilePath())); } catch (IOException ignored) {}
        vehicleImageRepository.delete(image);
    }

    public Vehicle getEntity(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }
}
