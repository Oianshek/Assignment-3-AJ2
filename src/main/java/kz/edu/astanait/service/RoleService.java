package kz.edu.astanait.service;

import kz.edu.astanait.model.Role;
import kz.edu.astanait.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleRepository roleRepo;

    public Role getRoleById(Long id){
        Role role = roleRepo.getOne(id);
        return role;
    }
}
