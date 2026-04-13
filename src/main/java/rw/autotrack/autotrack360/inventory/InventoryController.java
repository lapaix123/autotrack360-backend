package rw.autotrack.autotrack360.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/vehicle/{vehicleId}")
    public ResponseEntity<InventoryDTO> add(@PathVariable Long vehicleId,
                                            @RequestParam String location) {
        return ResponseEntity.ok(inventoryService.addToInventory(vehicleId, location));
    }

    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAll() {
        return ResponseEntity.ok(inventoryService.findAll());
    }
}
