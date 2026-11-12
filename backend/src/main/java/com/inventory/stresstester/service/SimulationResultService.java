package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.SimulationResult;
import com.inventory.stresstester.repository.SimulationResultRepository;
import org.springframework.stereotype.Service;
@Service
public class SimulationResultService extends CrudService<SimulationResult> {
    public SimulationResultService(SimulationResultRepository repository) { super(repository); }
}
