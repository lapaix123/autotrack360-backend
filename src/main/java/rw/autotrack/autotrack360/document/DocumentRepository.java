package rw.autotrack.autotrack360.document;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByRelatedTypeAndRelatedId(RelatedType relatedType, Long relatedId);
}
