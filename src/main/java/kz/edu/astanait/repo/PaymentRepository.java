package kz.edu.astanait.repo;

import kz.edu.astanait.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Query("SELECT p FROM Payment p WHERE p.account.id = ?1")
    List<Payment> findByAccountId(Long id);
}
