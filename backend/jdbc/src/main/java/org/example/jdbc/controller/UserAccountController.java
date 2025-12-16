package org.example.jdbc.controller;

import org.example.jdbc.entity.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<UserAccount>>> getAllUserAccounts() {
        List<UserAccount> userAccounts = userAccountService.getAllUserAccounts();
        return ResponseEntity.ok(ApiResponse.success(userAccounts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserAccount>> getUserAccountById(@PathVariable("id") String userId) {
        UserAccount userAccount = userAccountService.getUserAccountById(userId);
        if (userAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "用户账户不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(userAccount));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserAccount>> addUserAccount(@RequestBody UserAccount userAccount) {
        boolean success = userAccountService.addUserAccount(userAccount);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("用户账户添加成功", userAccount));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "用户账户添加失败"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserAccount>> updateUserAccount(@PathVariable("id") String userId, @RequestBody UserAccount userAccount) {
        userAccount.setUserId(userId);
        boolean success = userAccountService.updateUserAccount(userAccount);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("用户账户更新成功", userAccount));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "用户账户更新失败"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserAccount(@PathVariable("id") String userId) {
        boolean success = userAccountService.deleteUserAccount(userId);
        if (success) {
            return ResponseEntity.ok(ApiResponse.success("用户账户删除成功", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "用户账户删除失败"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserAccount>> login(@RequestBody UserAccount loginRequest) {
        UserAccount user = userAccountService.login(loginRequest.getUserId(), loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "用户名或密码错误"));
        }
        return ResponseEntity.ok(ApiResponse.success("登录成功", user));
    }
}