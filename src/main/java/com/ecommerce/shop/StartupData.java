package com.ecommerce.shop;

import com.ecommerce.shop.user.User;
import com.ecommerce.shop.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupData implements CommandLineRunner {
    private final UserService userService;

    @Autowired
    public StartupData(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        adminAccount();
        userAccount();
    }

    private void userAccount(){
        User user = new User();

        user.setUsername("user");
        user.setPassword("user");
        user.setPasswordConfirm("user");
        user.setGender("Female");
        user.setEmail("user@example.com");

        userService.save(user);
    }

    private void adminAccount(){
        User admin = new User();

        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setPasswordConfirm("admin");
        admin.setGender("Male");
        admin.setEmail("admin@example.com");

        userService.save(admin);
    }

}
