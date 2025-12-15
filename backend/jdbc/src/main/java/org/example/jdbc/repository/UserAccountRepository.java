package org.example.jdbc.repository;

import org.example.jdbc.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository extends CrudRepository<UserAccount, String> {
    UserAccount findByUserIdAndPassword(String userId, String password);
}