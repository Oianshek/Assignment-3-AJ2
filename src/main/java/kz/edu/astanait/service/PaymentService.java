package kz.edu.astanait.service;

import kz.edu.astanait.model.Payment;
import kz.edu.astanait.model.User;
import kz.edu.astanait.repo.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepo;

    public void save(Payment payment){
        paymentRepo.save(payment);
    }

    public List<Payment> getPaymentsByAccountId(Long id){
        return paymentRepo.findByAccountId(id);
    }

    public List<Payment> getAllPayments(){
        return paymentRepo.findAll();
    }
}
