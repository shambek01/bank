package com.example.bank.controllers;

import com.example.bank.models.Account;
import com.example.bank.models.Transaction;
import com.example.bank.models.TransactionForm;
import com.example.bank.models.User;
import com.example.bank.repositories.TransactionRepository;
import com.example.bank.repositories.UserRepository;
import com.example.bank.services.AccountService;
import com.example.bank.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;
    @GetMapping(path = "/transfer")
    public String getAllUserAccounts(Model model,@AuthenticationPrincipal UserDetails currentUser){
        User user = userRepository.findByUsername(currentUser.getUsername());
        model.addAttribute("accounts", user.getAccounts());
        for(Account el : user.getAccounts()){
            System.out.println(el.getCardName());
        }
        return "transfer";
    }
    @PostMapping(path = "/transfer")
    public String transfer(@ModelAttribute("transactionForm") TransactionForm transactionForm, Model model){
        transactionService.transfer(transactionForm);
        System.out.println(transactionForm.getReceiverID());
        System.out.println(transactionForm.getSenderID());
        System.out.println(transactionForm.getAmount());
        return "redirect:/";
    }
    @GetMapping(path = "/payment")
    public String allUserAccounts(Model model,@AuthenticationPrincipal UserDetails currentUser){
        User user =userRepository.findByUsername(currentUser.getUsername());
        model.addAttribute("accounts",user.getAccounts());
        return "payment";
    }
    @PostMapping(path = "/payment")
    public String payment(@ModelAttribute("transactionForm") TransactionForm transactionForm, Model model){
        transactionService.payment(transactionForm);
        return "redirect:/";
    }
    @GetMapping(path = "/history")
    public String getAllByAccount(@AuthenticationPrincipal UserDetails currentUser,Model model){
        User user = userRepository.findByUsername(currentUser.getUsername());
        System.out.println(transactionRepository.getAllByUser(user));
        for(Transaction el : transactionRepository.getAllByUser(user)){
            System.out.println(el.getId());
        }
        model.addAttribute("transactions",transactionRepository.getAllByUser(user));
        return "history";
    }
}
