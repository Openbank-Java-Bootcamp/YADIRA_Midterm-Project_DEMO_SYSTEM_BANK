package com.example.systembank.repository;

import com.example.systembank.model.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(nativeQuery = true, value = "SELECT type FROM account WHERE id = :id")
    Object findTypeAccountById(Long id);



}
