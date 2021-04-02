package com.example.bank.services;

import com.example.bank.models.Account;
import com.example.bank.models.Transaction;
import com.example.bank.models.TransactionForm;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    public void transfer(TransactionForm transactionForm){
        new TransactionHandler(transactionRepository,accountRepository,transactionForm);
    }
    public void payment(TransactionForm transactionForm){
        new PaymentHandler(transactionRepository,accountRepository,transactionForm);
    }
}
