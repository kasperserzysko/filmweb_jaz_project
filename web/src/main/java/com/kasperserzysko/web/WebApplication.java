package com.kasperserzysko.web;

import com.kasperserzysko.data.models.Role;
import com.kasperserzysko.data.models.User;
import com.kasperserzysko.data.repositories.DataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kasperserzysko.data", "com.kasperserzysko.web"})
public class WebApplication implements CommandLineRunner {

    private final DataRepository db;
    private final PasswordEncoder passwordEncoder;

    public WebApplication(DataRepository db, PasswordEncoder passwordEncoder) {
        this.db = db;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");
        var roleUser = new Role();
        roleUser.setName("ROLE_USER");

        var admin = new User();
        admin.setEmail("admin");
        admin.setPassword(passwordEncoder.encode("admin"));

        if(db.getRoles().getRole("ROLE_ADMIN").isEmpty()) {
            db.getRoles().save(roleAdmin);
            admin.addRole(roleAdmin);
        }
        if (db.getRoles().getRole("ROLE_USER").isEmpty()) {
            db.getRoles().save(roleUser);
            admin.addRole(roleUser);
        }
        if(db.getUsers().findUserByEmail("admin").isEmpty()){
            db.getUsers().save(admin);
        }
    }
}
