package com.example.systembank.repository;

import com.example.systembank.model.transactions.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findAllByDateBetween(Date start, Date end);

}
