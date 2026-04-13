package rw.autotrack.autotrack360.vehicle;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleDTO create(VehicleDTO dto) {
        Vehicle vehicle = Vehicle.builder()
                .vin(dto.getVin()).make(dto.getMake()).model(dto.getModel())
                .year(dto.getYear()).color(dto.getColor())
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
        vehicle.setPrice(dto.getPrice());
        return VehicleDTO.from(vehicleRepository.save(vehicle));
    }

    public VehicleDTO changeStatus(Long id, VehicleStatus status) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        vehicle.setStatus(status);
        return VehicleDTO.from(vehicleRepository.save(vehicle));
    }

    public Vehicle getEntity(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }
}
