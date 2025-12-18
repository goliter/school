package org.example.jdbc;

import org.example.jdbc.entity.UserAccount;
import org.example.jdbc.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 系统初始化类，用于在应用启动时创建默认的系统管理员账号
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public void run(String... args) throws Exception {
        // 检查系统管理员账号是否已存在
        if (userAccountRepository.findById("systemadmin").isEmpty()) {
            // 创建系统管理员账号
            UserAccount admin = new UserAccount();
            admin.setUserId("systemadmin");
            admin.setPassword("admin");
            admin.setRole("admin");
            admin.setStatus(1);
            admin.setCreateTime(new Date());
            
            // 保存到数据库
            userAccountRepository.save(admin);
            System.out.println("系统管理员账号已自动生成: user_id=systemadmin, password=admin");
        } else {
            System.out.println("系统管理员账号已存在，跳过创建");
        }
    }
}