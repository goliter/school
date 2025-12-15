package org.example.jdbc.controller;

import org.example.jdbc.entity.UserAccount;
import org.example.jdbc.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-accounts")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping
    public ResponseEntity<List<UserAccount>> getAllUserAccounts() {
        List<UserAccount> userAccounts = userAccountService.getAllUserAccounts();
        return new ResponseEntity<>(userAccounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> getUserAccountById(@PathVariable("id") String userId) {
        UserAccount userAccount = userAccountService.getUserAccountById(userId);
        if (userAccount == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userAccount, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addUserAccount(@RequestBody UserAccount userAccount) {
        boolean success = userAccountService.addUserAccount(userAccount);
        if (success) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserAccount(@PathVariable("id") String userId, @RequestBody UserAccount userAccount) {
        userAccount.setUserId(userId);
        boolean success = userAccountService.updateUserAccount(userAccount);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable("id") String userId) {
        boolean success = userAccountService.deleteUserAccount(userId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<UserAccount> login(@RequestBody UserAccount loginRequest) {
        UserAccount user = userAccountService.login(loginRequest.getUserId(), loginRequest.getPassword());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}