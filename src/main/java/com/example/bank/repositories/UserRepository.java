package com.example.bank.repositories;

import com.example.bank.models.Account;
import com.example.bank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
 }
