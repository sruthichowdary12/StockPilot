package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.SimulationRun;
import com.inventory.stresstester.repository.SimulationRunRepository;
import org.springframework.stereotype.Service;
@Service
public class SimulationRunService extends CrudService<SimulationRun> {
    public SimulationRunService(SimulationRunRepository repository) { super(repository); }
}
