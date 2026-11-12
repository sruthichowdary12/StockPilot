package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.Role;
import com.inventory.stresstester.repository.RoleRepository;
import org.springframework.stereotype.Service;
@Service
public class RoleService extends CrudService<Role> {
    public RoleService(RoleRepository repository) { super(repository); }
}
