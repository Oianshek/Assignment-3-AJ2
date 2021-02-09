package kz.edu.astanait.service;

import kz.edu.astanait.model.Account;
import kz.edu.astanait.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository accountRepo;

    public void save(Account account){
        accountRepo.save(account);
    }
}
