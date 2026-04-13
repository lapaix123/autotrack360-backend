package rw.autotrack.autotrack360.sales;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    long countByStatus(SaleStatus status);
}
