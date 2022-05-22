package com.example.systembank.service.impl;

import com.example.systembank.DTO.AccountHolderDTO;
import com.example.systembank.model.accounts.Account;
import com.example.systembank.model.users.AccountHolder;
import com.example.systembank.model.users.Role;
import com.example.systembank.model.users.User;
import com.example.systembank.repository.UserRepository;
import com.example.systembank.repository.RoleRepository;
import com.example.systembank.service.interfaces.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserServiceInterface, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveRole(Role role) throws Exception {
        Role roleDB = roleRepository.findByName(role.getName());
        if(roleDB == null) {
            log.info("Saving a new role {} to the database", role.getName());
            roleRepository.save(role);
        } else {
            throw new Exception("The role already exist");
        }
    }

    @Override
    public void createAccountHolder(AccountHolderDTO accHoldDTO) throws Exception {

        String nameAH = accHoldDTO.getName();
        Optional<User> optionalAccountHolder = userRepository.findByName(nameAH);
        if(optionalAccountHolder.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The AccountHolder already exist.");
        }

        String usernameAH = accHoldDTO.getUsername();
        optionalAccountHolder = userRepository.findByUsername(usernameAH);
        if(optionalAccountHolder.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username already exist.");
        }

        try{
            log.info("Saving new user {} to the database", accHoldDTO.getName());
            accHoldDTO.setPassword(passwordEncoder.encode(accHoldDTO.getPassword()));

            AccountHolder newAccHold = new AccountHolder(accHoldDTO.getName(), accHoldDTO.getUsername(), accHoldDTO.getPassword(),
                    new SimpleDateFormat("yyyy-MM-dd").parse(accHoldDTO.getDateOfBirth()), accHoldDTO.getPrimaryAddress(), accHoldDTO.getMailingAddress());
            Role role = roleRepository.findByName("ACCOUNT_HOLDER");
            newAccHold.setRole(role);
            userRepository.save(newAccHold);

        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The date format is wrong.");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optResult = userRepository.findByUsername( username );
        if (optResult.isEmpty()) {
            throw new UsernameNotFoundException( "No User with username = '" + username + "' was found." );
        }

        User user = optResult.get();
        log.info( user.toString() );

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities
        );
    }

}











