package com.example.bank.repositories;

import com.example.bank.models.Transaction;
import com.example.bank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Set<Transaction> getAllByUser(User user);
}
