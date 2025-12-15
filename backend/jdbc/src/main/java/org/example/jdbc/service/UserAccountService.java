package org.example.jdbc.service;

import org.example.jdbc.entity.UserAccount;
import java.util.List;

public interface UserAccountService {
    List<UserAccount> getAllUserAccounts();
    UserAccount getUserAccountById(String userId);
    UserAccount login(String userId, String password);
    boolean addUserAccount(UserAccount userAccount);
    boolean updateUserAccount(UserAccount userAccount);
    boolean deleteUserAccount(String userId);
}