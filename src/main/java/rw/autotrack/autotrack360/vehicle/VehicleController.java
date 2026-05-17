package rw.autotrack.autotrack360.vehicle;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleDTO> create(@RequestBody VehicleDTO dto) {
        return ResponseEntity.ok(vehicleService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAll(@RequestParam(required = false) VehicleStatus status) {
        if (status != null) {
            return ResponseEntity.ok(vehicleService.findAll().stream()
                    .filter(v -> v.getStatus() == status).toList());
        }
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> update(@PathVariable Long id, @RequestBody VehicleDTO dto) {
        return ResponseEntity.ok(vehicleService.update(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VehicleDTO> changeStatus(@PathVariable Long id, @RequestParam VehicleStatus status) {
        return ResponseEntity.ok(vehicleService.changeStatus(id, status));
    }

    // ── Images ──────────────────────────────────────────────

    @PostMapping("/{id}/images")
    public ResponseEntity<VehicleImageDTO> uploadImage(@PathVariable Long id,
                                                       @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(vehicleService.uploadImage(id, file));
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<List<VehicleImageDTO>> getImages(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getImages(id));
    }

    @GetMapping("/{id}/images/{filename}")
    public ResponseEntity<Resource> serveImage(@PathVariable Long id,
                                               @PathVariable String filename) throws IOException {
        Resource resource = vehicleService.serveImage(id, filename);
        String contentType = filename.toLowerCase().endsWith(".png") ? "image/png" : "image/jpeg";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        vehicleService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}
