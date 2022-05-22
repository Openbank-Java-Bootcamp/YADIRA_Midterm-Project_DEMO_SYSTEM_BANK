package com.example.systembank.controller.impl;

import com.example.systembank.model.users.Role;
import com.example.systembank.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRole(@RequestBody Role role) throws Exception {
        userService.saveRole(role);
    }

}
