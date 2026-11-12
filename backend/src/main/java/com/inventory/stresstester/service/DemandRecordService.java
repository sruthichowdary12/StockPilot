package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.DemandRecord;
import com.inventory.stresstester.repository.DemandRecordRepository;
import org.springframework.stereotype.Service;
@Service
public class DemandRecordService extends CrudService<DemandRecord> {
    public DemandRecordService(DemandRecordRepository repository) { super(repository); }
}
