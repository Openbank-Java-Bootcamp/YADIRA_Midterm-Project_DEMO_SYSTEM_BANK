package com.example.systembank.controller.impl;

import com.example.systembank.DTO.AccountHolderDTO;
import com.example.systembank.DTO.PartialAccountDTO;
import com.example.systembank.controller.interfaces.AccountHolderControllerInterface;
import com.example.systembank.model.users.User;
import com.example.systembank.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/account_holders")
public class AccountHolderController implements AccountHolderControllerInterface {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccountHolder(@RequestBody @Valid AccountHolderDTO accountHolderDTO) throws Exception {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        /*if (auth.isAuthenticated()) {
            log.info( " User is authenticated " );
            for (GrantedAuthority role: auth.getAuthorities()) {
                log.info("Role: " + role.getAuthority());
            }
            log.info( auth.getPrincipal().toString() );*/
            userService.createAccountHolder(accountHolderDTO);
        /*} else {
            log.info( " User is not authenticated " );
        }*/
    }

}
