package com.inventory.stresstester.repository;
import com.inventory.stresstester.entity.SimulationRun;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SimulationRunRepository extends JpaRepository<SimulationRun, Long> {}
