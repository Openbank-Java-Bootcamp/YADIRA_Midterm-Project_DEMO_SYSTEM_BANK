package com.example.systembank.service.interfaces;

import com.example.systembank.DTO.TransferAccToAccDTO;
import com.example.systembank.DTO.TransferAccToThirdPartyDTO;
import com.example.systembank.DTO.TransferThirdPartyToAccDTO;

public interface TransferServiceInterface {
    void transferFromAccountToAccount(Long id, String username, TransferAccToAccDTO transferAccToAccDTO);

    void transferFromAccountToThirdParty(Long id, String username, TransferAccToThirdPartyDTO transferAccountToThirdPartyDTO);

    void transferFromThirdPartyToAccount(String hashedKey, TransferThirdPartyToAccDTO transferThirdPartyToAccDTO);

    }
