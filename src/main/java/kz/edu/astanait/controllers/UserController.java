package kz.edu.astanait.controllers;

import kz.edu.astanait.configs.MyUserDetails;
import kz.edu.astanait.model.Account;
import kz.edu.astanait.model.Payment;
import kz.edu.astanait.model.Role;
import kz.edu.astanait.model.User;
import kz.edu.astanait.service.PaymentService;
import kz.edu.astanait.service.RoleService;
import kz.edu.astanait.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    PaymentService paymentService;

    @GetMapping
    public String getIndex(){
        return "index";
    }

    @GetMapping("/getAll")
    public List<User> getAll(){
        return userService.getAllUsers();
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

    @PostMapping("/add")
    public void add(@RequestBody User user) {
        userService.addUser(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody User user, @PathVariable Long id) {
        try {
            User existUser = userService.getUserById(id);
            user.setId(id);
            userService.addUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new User());

        return "registerForm";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        Role role = roleService.getRoleById(2L);
        user.setRole(role);
        user.setAccount(new Account(dtf.format(now),0L));
        user.setPassword(encodedPassword);
        userService.addUser(user);

        return "index";
    }

    @PostMapping("/change_password")
    public String processChangePassword(HttpServletRequest request,Model model,RedirectAttributes redirectAttributes) throws ServletException {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = myUserDetails.getUser();
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");

        if(oldPassword.equals(newPassword)) {
            model.addAttribute("message", "Your newPassword must not be the same as the oldest password that you used!");
            return "change_password";
        }
        if(!passwordEncoder.matches(oldPassword,user.getPassword())) {
            model.addAttribute("message", "Your old password is incorrect, please try again...");
            return "change_password";
        } else {
            userService.changePassword(user,passwordEncoder.encode(newPassword));
            request.logout();
            redirectAttributes.addFlashAttribute("message", "You have changed your password successfully. "
                    + "Please login again.");
        }

        return "redirect:/login";
    }

    @GetMapping("/changePassword")
    public String showEditPasswordForm(){
        return "change_password";
    }

    @GetMapping("/services")
    public String showServices(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if(currentUser != null){
            return "services";
        }else {
            return "redirect:/login";
        }
    }

    @GetMapping("/show_profile")
    public String showProfile(){
        return "profile";
    }

    @GetMapping("/showHistory")
    public String showHistory(Model model, Principal principal){
        User user = userService.getUserByEmail(principal.getName());
        Account account = user.getAccount();
        List<Payment> payments = paymentService.getPaymentsByAccountId(account.getId());

        model.addAttribute("payments",payments);
        return "history";
    }

    @GetMapping("/getAllPayments")
    public String showAllPayments(Model model){
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);

        return "showAllPayments";
    }

    @GetMapping("/showAllUsers")
    public String showAllUsers(Model model){
        List<User> users = userService.getAllUsers();
        model.addAttribute("users",users);

        return "showAllUsers";
    }
}
