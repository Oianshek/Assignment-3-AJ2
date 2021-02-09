package kz.edu.astanait.controllers;

import kz.edu.astanait.PaymentLogger;
import kz.edu.astanait.model.Account;
import kz.edu.astanait.model.Payment;
import kz.edu.astanait.model.User;
import kz.edu.astanait.service.AccountService;
import kz.edu.astanait.service.PaymentService;
import kz.edu.astanait.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
    @Autowired
    PaymentService paymentService;

    @GetMapping("/toAnotherBank")
    public String toAnotherBank(){
        return "anotherbank";
    }

    @PostMapping("/toAnotherBank")
    public String processToAnotherBank(HttpServletRequest request, Model model, Principal principal){
        Long amount = Long.valueOf(request.getParameter("amount"));
        String cardNumber = request.getParameter("card");
        User user = userService.getUserByEmail(principal.getName());
        Account account = user.getAccount();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();

        if (account.getAmountOfMoney() > amount) {
            amount+=amount/100;
            account.setAmountOfMoney(account.getAmountOfMoney() - amount);
            accountService.save(account);
            Payment payment = new Payment(dtf.format(now),amount,cardNumber,account);
            paymentService.save(payment);

            new PaymentLogger(payment).start();

            return "index";
        }else{
            model.addAttribute("msg","Not enough money!");
            return "anotherbank";
        }
    }

    @GetMapping("/toThisBank")
    public String toThisBank(){
        return "thisbank";
    }

    @PostMapping("/toThisBank")
    public String processToThisBank(HttpServletRequest request, Model model, Principal principal){
        Long amount = Long.valueOf(request.getParameter("amount"));
        String cardNumber = request.getParameter("card");
        User user = userService.getUserByEmail(principal.getName());
        Account account = user.getAccount();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();

        if (account.getAmountOfMoney() > amount) {
            if(amount>100000){
                amount+= amount/100;
                account.setAmountOfMoney(account.getAmountOfMoney() - amount);
            }else{
                account.setAmountOfMoney(account.getAmountOfMoney() - amount);
            }
            accountService.save(account);
            Payment payment = new Payment(dtf.format(now),amount,cardNumber+" (AITU BANK)",account);
            paymentService.save(payment);

            new PaymentLogger(payment).start();

            return "index";
        }else{
            model.addAttribute("msg","Not enough money!");
            return "thisbank";
        }
    }

    @GetMapping("/converted_version")
    public String showConverted(Model model, Principal principal){
        User user = userService.getUserByEmail(principal.getName());
        Account account = user.getAccount();
        Map<String,Long> maps = new HashMap<>();
        maps.put("KZT", account.getAmountOfMoney());
        maps.put("USD",account.getAmountOfMoney()/419);
        maps.put("EUR",account.getAmountOfMoney()/507);

        model.addAttribute("maps",maps);
        return "convertion";
    }
//    @PostMapping("/toThisBank")
//    public String processToThisBank(@ModelAttribute("amount") String money, Principal principal){
//        System.out.println(money);
//        return "index";
//    }
}
