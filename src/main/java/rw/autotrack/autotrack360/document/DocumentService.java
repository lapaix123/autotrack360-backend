package rw.autotrack.autotrack360.document;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private final DocumentRepository documentRepository;

    public DocumentDTO upload(MultipartFile file, RelatedType relatedType, Long relatedId) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Document doc = Document.builder()
                .name(file.getOriginalFilename())
                .filePath(filePath.toString())
                .relatedType(relatedType)
                .relatedId(relatedId)
                .build();
        return DocumentDTO.from(documentRepository.save(doc));
    }

    public Resource download(Long id) throws IOException {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        Path path = Paths.get(doc.getFilePath());
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists()) throw new RuntimeException("File not found on disk");
        return resource;
    }

    public List<DocumentDTO> findByRelated(RelatedType relatedType, Long relatedId) {
        return documentRepository.findByRelatedTypeAndRelatedId(relatedType, relatedId)
                .stream().map(DocumentDTO::from).collect(Collectors.toList());
    }
}
