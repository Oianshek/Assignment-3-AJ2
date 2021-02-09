package kz.edu.astanait.controllers;

import kz.edu.astanait.model.Payment;
import kz.edu.astanait.model.User;
import kz.edu.astanait.service.PaymentService;
import kz.edu.astanait.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class MyRestController {
    @Autowired
    UserService userService;
    @Autowired
    PaymentService paymentService;

    @GetMapping("/getAll")
    public List<User> getAll(){
        return userService.getAllUsers();
    }

    @GetMapping("/getAllPayments")
    public List<Payment> getAllPayments(){
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id){
        try{
            User user = userService.getUserById(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ajax_check")
    public @ResponseBody User getUser(@RequestParam("email") String email){
        return userService.getUserByEmail(email);
    }
}
