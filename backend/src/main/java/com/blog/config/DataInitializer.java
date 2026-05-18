package com.blog.config;

import com.blog.entity.Role;
import com.blog.entity.User;
import com.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("====== 开始初始化数据 ======");
        
        // 先删除旧用户（如果存在）
        userRepository.findByUsername("admin").ifPresent(existing -> {
            System.out.println("删除旧用户: " + existing.getUsername());
            userRepository.delete(existing);
        });
        
        // 创建新用户
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("admin@blog.com")
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
        System.out.println("====== 默认用户已创建: admin / admin ======");
    }
}
