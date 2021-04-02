package com.example.bank.services;

import com.example.bank.models.Account;
import com.example.bank.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    public void addAccount(Account account){
        accountRepository.save(account);
    }
}
