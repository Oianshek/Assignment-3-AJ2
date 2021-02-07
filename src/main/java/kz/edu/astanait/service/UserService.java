package kz.edu.astanait.service;

import kz.edu.astanait.model.User;
import kz.edu.astanait.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public void addUser(User user){
        userRepo.save(user);
    }

    public void deleteUser(User user){
        userRepo.delete(user);
    }

    public User getUserById(Long id){
        return userRepo.findById(id).get();
    }

    public User getUserByEmail(String email){
        User user = userRepo.findByEmail(email);

        return user;
    }
}
