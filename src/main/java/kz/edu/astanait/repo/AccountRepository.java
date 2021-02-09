package kz.edu.astanait.repo;

import kz.edu.astanait.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
}
