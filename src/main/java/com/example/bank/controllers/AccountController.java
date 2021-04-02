package com.example.bank.controllers;

import com.example.bank.models.Account;
import com.example.bank.models.User;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.UserRepository;
import com.example.bank.services.AccountService;
import com.example.bank.services.UserService;
import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Controller
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @GetMapping(path = "/registeraccount")
    public String registerAccount(Model model,@AuthenticationPrincipal UserDetails currentUser){
        model.addAttribute(("accountForm"), new Account());
        return "registeraccount";
    }
    @PostMapping(path = "/registeraccount")
    public String addAccount(@ModelAttribute("accountForm") Account account,Model model,@AuthenticationPrincipal UserDetails currentUser){
        User user = userRepository.findByUsername(currentUser.getUsername());
        account.setUser(user);
        accountService.addAccount(account);
        Set<Account> accounts = user.getAccounts();
        accounts.add(account);
        user.setAccounts(accounts);
        userRepository.save(user);
        return "redirect:/";
    }
    @GetMapping(path = "/allaccounts")
    public String getAllUserAccounts(Model model,@AuthenticationPrincipal UserDetails currentUser){
        User user = userRepository.findByUsername(currentUser.getUsername());
        model.addAttribute("accounts", user.getAccounts());
        for(Account el : user.getAccounts()){
            System.out.println(el.getCardName());
        }
        return "allaccounts";
    }
}
