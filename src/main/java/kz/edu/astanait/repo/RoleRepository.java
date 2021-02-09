package kz.edu.astanait.repo;

import kz.edu.astanait.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
