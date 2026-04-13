package rw.autotrack.autotrack360.document;

import lombok.Data;

@Data
public class DocumentDTO {
    private Long id;
    private String name;
    private String filePath;
    private RelatedType relatedType;
    private Long relatedId;

    public static DocumentDTO from(Document d) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(d.getId());
        dto.setName(d.getName());
        dto.setFilePath(d.getFilePath());
        dto.setRelatedType(d.getRelatedType());
        dto.setRelatedId(d.getRelatedId());
        return dto;
    }
}
