package com.example.systembank.service.interfaces;

import com.example.systembank.DTO.AccountHolderDTO;
import com.example.systembank.model.users.Role;

public interface UserServiceInterface {

   void saveRole(Role role) throws Exception;
   void createAccountHolder(AccountHolderDTO accountHolderDTO) throws Exception;



}
