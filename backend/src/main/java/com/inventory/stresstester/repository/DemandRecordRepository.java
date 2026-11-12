
package com.inventory.stresstester.repository;

import com.inventory.stresstester.entity.DemandRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandRecordRepository
        extends JpaRepository<DemandRecord, Long> {

    List<DemandRecord> findByProduct_IdOrderByDemandDateAsc(
            Long productId
    );
}
