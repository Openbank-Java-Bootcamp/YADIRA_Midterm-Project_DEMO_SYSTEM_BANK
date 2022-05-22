package com.example.systembank.controller.impl;

import com.example.systembank.DTO.TransferAccToAccDTO;
import com.example.systembank.DTO.TransferAccToThirdPartyDTO;
import com.example.systembank.DTO.TransferThirdPartyToAccDTO;
import com.example.systembank.controller.interfaces.TransferControllerInterface;
import com.example.systembank.service.impl.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/transfers")
public class TransferController implements TransferControllerInterface {

    @Autowired
    private TransferService transferService;

    @PostMapping("/account-account/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void transferFromAccountToAccount(@PathVariable(name = "id") Long id, @RequestBody @Valid TransferAccToAccDTO transferAccToAccDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();
        transferService.transferFromAccountToAccount(id, username, transferAccToAccDTO);
    }

    @PostMapping("/account-third_party/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void transferFromAccountToThirdParty(@PathVariable(name = "id") Long id, @RequestBody @Valid TransferAccToThirdPartyDTO transferAccountToThirdPartyDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();
        transferService.transferFromAccountToThirdParty(id, username, transferAccountToThirdPartyDTO);
    }

    @PostMapping("/third_party-account/{hashedKey}")
    @ResponseStatus(HttpStatus.CREATED)
    public void transferFromThirdPartyToAccount(@PathVariable(name = "hashedKey") String hashedKey, @RequestBody @Valid TransferThirdPartyToAccDTO transferThirdPartyToAccDTO) {
             transferService.transferFromThirdPartyToAccount(hashedKey, transferThirdPartyToAccDTO);
    }
}
