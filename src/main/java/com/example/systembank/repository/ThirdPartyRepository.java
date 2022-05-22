package com.example.systembank.repository;

import com.example.systembank.model.accounts.ThirdParty;
import com.example.systembank.model.users.AccountHolder;
import com.example.systembank.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {

    Optional<ThirdParty> findByName(String name);

    Optional<ThirdParty> findByHashedKey(String hashedKey);

    @Query(nativeQuery = true, value = "SELECT * FROM third_party WHERE hashed_key = :hashKey")
    Optional<ThirdParty> findByTPHashedKey(String hashKey);

}
