package com.hackathon.finservice.repository;

import com.hackathon.finservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.user.email = :email")
    List<Account> findAllByEmail(String email);

    @Query("SELECT a FROM Account a WHERE a.number = :accountNumber")
    Optional<Account> findByNumber(String accountNumber);

    List<Account> findByType(String type);

    default Optional<Account> findByEmailAndIndex(String email, int index) {
        List<Account> accounts = findAllByEmail(email);
        if (index >= 0 && index < accounts.size()) {
            return Optional.of(accounts.get(index));
        }
        return Optional.empty();
    }
}
