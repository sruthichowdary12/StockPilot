package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.Scenario;
import com.inventory.stresstester.repository.ScenarioRepository;
import org.springframework.stereotype.Service;
@Service
public class ScenarioService extends CrudService<Scenario> {
    public ScenarioService(ScenarioRepository repository) { super(repository); }
}
