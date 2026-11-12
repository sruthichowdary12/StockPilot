package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.AppUser;
import com.inventory.stresstester.repository.AppUserRepository;
import org.springframework.stereotype.Service;
@Service
public class AppUserService extends CrudService<AppUser> {
    public AppUserService(AppUserRepository repository) { super(repository); }
}
