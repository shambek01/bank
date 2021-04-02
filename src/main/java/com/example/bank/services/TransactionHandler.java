package com.example.bank.services;

import com.example.bank.models.Account;
import com.example.bank.models.Transaction;
import com.example.bank.models.TransactionForm;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.TransactionRepository;
import org.hibernate.query.criteria.internal.expression.function.CurrentDateFunction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class TransactionHandler extends Thread {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private TransactionForm transactionForm;

    public TransactionHandler(TransactionRepository transactionRepository, AccountRepository accountRepository, TransactionForm transactionForm) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionForm = transactionForm;
        start();
    }

    public void run() {
        Optional<Account> receiverOpt = accountRepository.findById(transactionForm.getReceiverID());
        System.out.println(receiverOpt.get().getCurrency());
        Optional<Account> senderOpt = accountRepository.findById(transactionForm.getSenderID());
        float amount = transactionForm.getAmount();
        if (senderOpt.isPresent() && receiverOpt.isPresent()) {
            if (amount > 0) {
                Account sender = senderOpt.get();
                Account receiver = receiverOpt.get();
                transfer(sender, receiver, amount);
            }
        }


    }

    public synchronized String transfer(Account sender, Account receiver, float amount) {
        if ((sender.getAccountID() == receiver.getAccountID()) || ((amount < 100000) && (sender.getBank().equals(receiver.getBank())))) {
            if (sender.getCurrency().equals(receiver.getCurrency())) {
                receiver.setAccountBalance(receiver.getAccountBalance() + amount);
                sender.setAccountBalance(sender.getAccountBalance() - amount);
            } else if (sender.getCurrency() == "USD") {
                if (receiver.getCurrency() == "EUR") {
                    receiver.setAccountBalance((float) (receiver.getAccountBalance() + (amount / 1.2)));
                    sender.setAccountBalance((float) (sender.getAccountBalance() - amount));
                }
                if (receiver.getCurrency() == "KZT") {
                    receiver.setAccountBalance(receiver.getAccountBalance() + (amount + (amount * 506)));
                    sender.setAccountBalance((float) (sender.getAccountBalance() - amount));
                }
            } else if (sender.getCurrency() == "EUR") {
                if (receiver.getCurrency() == "KZT") {
                    receiver.setAccountBalance((float) (receiver.getAccountBalance() + (amount / 506)));
                    sender.setAccountBalance((float) (sender.getAccountBalance() - amount));
                }
                if (receiver.getCurrency() == "USD") {
                    receiver.setAccountBalance((float) (receiver.getAccountBalance() + (amount * 1.2)));
                    sender.setAccountBalance((float) (sender.getAccountBalance() - amount));
                }
            } else if (sender.getCurrency() == "KZT") {
                if (receiver.getCurrency() == "EUR") {
                    receiver.setAccountBalance((float) (receiver.getAccountBalance() + (amount / 506)));
                    sender.setAccountBalance((float) (sender.getAccountBalance() - amount));
                }
                if (receiver.getCurrency() == "USD") {
                    receiver.setAccountBalance((float) (receiver.getAccountBalance() + (amount / 426)));
                    sender.setAccountBalance((float) (sender.getAccountBalance() - amount));
                }
            }
        } else {
            if (sender.getCurrency().equals(receiver.getCurrency())) {
                receiver.setAccountBalance(receiver.getAccountBalance() + amount);
                sender.setAccountBalance((float) (sender.getAccountBalance() - amount*1.1));
            } else if (sender.getCurrency() == "USD") {
                if (receiver.getCurrency() == "EUR") {
                    receiver.setAccountBalance((float) (receiver.getAccountBalance() + (amount / 1.2)));
                    sender.setAccountBalance((float) (sender.getAccountBalance() - amount * 1.1));
                }
                if (receiver.getCurrency() == "KZT") {
                    receiver.setAccountBalance(receiver.getAccountBalance() + (amount + (amount * 506)));
                    sender.setAccountBalance((float) (sender.getAccountBalance() - amount * 1.1));
                }
            } else if (sender.getCurrency() == "EUR") {
                if (receiver.getCurrency() == "KZT") {
                    receiver.setAccountBalance((float) (receiver.getAccountBalance() + (amount / 506)));
                    sender.setAccountBalance((float) (sender.getAccountBalance() - amount * 1.1));
                }
                if (receiver.getCurrency() == "USD") {
                    receiver.setAccountBalance((float) (receiver.getAccountBalance() + (amount * 1.2)));
                    sender.setAccountBalance((float) (sender.getAccountBalance() - amount * 1.1));
                }
            } else if (sender.getCurrency() == "KZT") {
                if (receiver.getCurrency() == "EUR") {
                    receiver.setAccountBalance((float) (receiver.getAccountBalance() + (amount / 506)));
                    sender.setAccountBalance((float) (sender.getAccountBalance() - amount * 1.1));
                }
                if (receiver.getCurrency() == "USD") {
                    receiver.setAccountBalance((float) (receiver.getAccountBalance() + (amount / 426)));
                    sender.setAccountBalance((float) (sender.getAccountBalance() - amount));
                }
            }
        }
        if (sender.getAccountBalance() < 0) {
            return "Not enough money";
        } else {
            accountRepository.save(sender);
            accountRepository.save(receiver);
            long millis = System.currentTimeMillis();
            Date date = new Date(millis);
            Transaction transactionRec = new Transaction();
            Transaction transactionSen = new Transaction();
            transactionRec.setAmount(amount);
            transactionSen.setAmount(amount);
            transactionRec.setType("Receive");
            transactionSen.setType("Send");
            transactionRec.setDate(date);
            transactionSen.setDate(date);
            transactionRec.setAccount(accountRepository.findById(transactionForm.getReceiverID()).get());
            transactionSen.setAccount(accountRepository.findById(transactionForm.getSenderID()).get());
            transactionRec.setUser(receiver.getUser());
            transactionSen.setUser(sender.getUser());
            log(transactionRec);
            log(transactionSen);
            transactionRepository.save(transactionRec);
            transactionRepository.save(transactionSen);
            return "0";
        }
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
