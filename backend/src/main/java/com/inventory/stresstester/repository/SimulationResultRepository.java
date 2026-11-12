
package com.inventory.stresstester.repository;

import com.inventory.stresstester.entity.SimulationResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulationResultRepository
        extends JpaRepository<SimulationResult, Long> {

    List<SimulationResult>
    findBySimulationRun_IdOrderBySimulationDayAsc(Long runId);
}
