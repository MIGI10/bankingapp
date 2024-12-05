package com.hackathon.finservice.Repositories;

import com.hackathon.finservice.Entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.user.email = :email")
    List<Account> findAllByEmail(String email);

    default Optional<Account> findByEmailAndIndex(String email, int index) {
        List<Account> accounts = findAllByEmail(email);
        if (index >= 0 && index < accounts.size()) {
            return Optional.of(accounts.get(index));
        }
        return Optional.empty();
    }
}
