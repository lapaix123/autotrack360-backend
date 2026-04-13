package rw.autotrack.autotrack360.document;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Document {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String filePath;

    @Enumerated(EnumType.STRING)
    private RelatedType relatedType;

    private Long relatedId;
}
