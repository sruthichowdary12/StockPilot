package com.inventory.stresstester.repository;
import com.inventory.stresstester.entity.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {}
