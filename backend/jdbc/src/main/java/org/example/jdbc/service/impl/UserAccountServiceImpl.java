package org.example.jdbc.service.impl;

import org.example.jdbc.entity.UserAccount;
import org.example.jdbc.repository.UserAccountRepository;
import org.example.jdbc.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public List<UserAccount> getAllUserAccounts() {
        return (List<UserAccount>) userAccountRepository.findAll();
    }

    @Override
    public UserAccount getUserAccountById(String userId) {
        Optional<UserAccount> userAccount = userAccountRepository.findById(userId);
        return userAccount.orElse(null);
    }

    @Override
    public UserAccount login(String userId, String password) {
        return userAccountRepository.findByUserIdAndPassword(userId, password);
    }

    @Override
    public boolean addUserAccount(UserAccount userAccount) {
        try {
            userAccountRepository.save(userAccount);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateUserAccount(UserAccount userAccount) {
        try {
            if (userAccountRepository.existsById(userAccount.getUserId())) {
                // 获取当前用户账户信息
                UserAccount currentUser = userAccountRepository.findById(userAccount.getUserId()).orElse(null);
                if (currentUser != null) {
                    // 保留原有的角色信息
                    userAccount.setRole(currentUser.getRole());
                    // 保存更新后的用户账户
                    userAccountRepository.save(userAccount);
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteUserAccount(String userId) {
        try {
            if (userAccountRepository.existsById(userId)) {
                userAccountRepository.deleteById(userId);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}