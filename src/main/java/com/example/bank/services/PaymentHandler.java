package com.example.bank.services;

import com.example.bank.models.Account;
import com.example.bank.models.Transaction;
import com.example.bank.models.TransactionForm;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.TransactionRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class PaymentHandler extends Thread{
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private TransactionForm transactionForm;

    public PaymentHandler(TransactionRepository transactionRepository, AccountRepository accountRepository, TransactionForm transactionForm) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionForm = transactionForm;
        start();
    }

    public void run(){
        Optional<Account> senderOpt = accountRepository.findById(transactionForm.getSenderID());
        float amount = transactionForm.getAmount();
        if (senderOpt.isPresent()) {
            if (amount > 0) {
                Account sender = senderOpt.get();
                payment(sender, amount);
            }
        }


    }
    public synchronized String payment(Account sender,float amount){
        if(sender.getCurrency().equals("USD")){
            amount/=426;

        }
        else if(sender.getCurrency().equals("EUR")){
            amount/=506;
        }
        sender.setAccountBalance(sender.getAccountBalance()-amount);
        if(sender.getAccountBalance()<0){
            return "Not enough money";
        }
        Transaction pay = new Transaction();
        pay.setAccount(accountRepository.findById(transactionForm.getSenderID()).get());
        pay.setAmount(amount);
        pay.setType("Payment");
        long millis=System.currentTimeMillis();
        Date date = new Date(millis);
        pay.setDate(date);
        pay.setUser(pay.getAccount().getUser());
        transactionRepository.save(pay);
        log(pay);
        accountRepository.save(sender);
        return "Success";
    }
    public void log(Transaction transaction) {
        File file = new File("C:\\Users\\Home\\Desktop\\log.txt");
        String log = "transaction_id=" + transaction.getId() + " " + "currency=" + transaction.getAccount().getCurrency()
                + " " + "amount=" + transaction.getAmount() + " " + "account_id=" + transaction.getAccount().getAccountID() + " "
                + "type=" + transaction.getType();
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.append(log);
            fileWriter.append(System.getProperty("line.separator"));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
