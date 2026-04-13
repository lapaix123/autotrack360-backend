package rw.autotrack.autotrack360.document;

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
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> upload(
            @RequestParam MultipartFile file,
            @RequestParam RelatedType relatedType,
            @RequestParam Long relatedId) throws IOException {
        return ResponseEntity.ok(documentService.upload(file, relatedType, relatedId));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {
        Resource resource = documentService.download(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getByRelated(
            @RequestParam RelatedType relatedType,
            @RequestParam Long relatedId) {
        return ResponseEntity.ok(documentService.findByRelated(relatedType, relatedId));
    }
}
