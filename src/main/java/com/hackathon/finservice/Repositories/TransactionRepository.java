package com.hackathon.finservice.Repositories;

import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT a FROM Transaction a WHERE a.sourceAccount.number = :sourceAccountNumber")
    List<Transaction> findAllBySourceAccount(String sourceAccountNumber);
}
