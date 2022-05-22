package com.example.systembank;

import com.example.systembank.model.accounts.ThirdParty;
import com.example.systembank.model.transactions.Transfer;
import com.example.systembank.repository.ThirdPartyRepository;
import com.example.systembank.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.sql.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootApplication
public class SystemBankApplication implements CommandLineRunner {



	public static void main(String[] args) {
		SpringApplication.run(SystemBankApplication.class, args);
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(String... args) throws Exception {

	}
}

