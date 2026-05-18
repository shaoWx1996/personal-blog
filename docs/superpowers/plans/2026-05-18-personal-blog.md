# Personal Blog Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a personal blog with Spring Boot backend and Vue 3 frontend, supporting article management, categories, tags, comments, and search functionality.

**Architecture:** Hybrid architecture with Spring Boot REST API serving Vue 3 SPA. Frontend built and deployed as static resources within the Spring Boot application.

**Tech Stack:** Java 17+, Spring Boot 3.x, Spring Data JPA, MySQL 8.0+, Vue 3 + Vite, Vue Router, Pinia, Axios

---

## 整体任务结构

| 阶段 | 任务数 | 预估耗时 | 依赖 |
|------|--------|----------|------|
| 环境准备 | 2 | 30 min | - |
| 后端初始化 | 5 | 60 min | 环境准备 |
| 数据库配置 | 3 | 20 min | 后端初始化 |
| 实体类创建 | 6 | 40 min | 数据库配置 |
| Repository层 | 5 | 25 min | 实体类 |
| DTO层 | 8 | 30 min | 实体类 |
| Service层 | 5 | 60 min | Repository + DTO |
| Controller层 | 6 | 45 min | Service |
| 安全配置 | 3 | 30 min | Controller |
| 前端初始化 | 3 | 20 min | - |
| 前端组件 | 8 | 90 min | 前端初始化 |
| 前端页面 | 6 | 60 min | 组件 |
| 测试与构建 | 2 | 30 min | 所有任务 |

---

## 阶段一：环境准备

### 任务 1.1：创建项目目录结构

**Files:**
- Create: `backend/`
- Create: `frontend/`
- Create: `docs/superpowers/plans/` (已存在)

- [ ] **Step 1: 创建目录**

```bash
mkdir -p backend/src/main/java/com/blog/{controller,service,repository,entity,dto,config,security}
mkdir -p backend/src/main/resources/{static,db/migration}
mkdir -p backend/src/test/java/com/blog
mkdir -p frontend/src/{components,views,router,store,api}
```

- [ ] **Step 2: 验证目录结构**

```bash
ls -la backend/src/main/java/com/blog/
ls -la frontend/src/
```

预期输出：显示所有创建的子目录

- [ ] **Step 3: 估算耗时** - 5 min

---

### 任务 1.2：创建 pom.xml 和 package.json

**Files:**
- Create: `backend/pom.xml`
- Create: `frontend/package.json`
- Create: `frontend/vite.config.js`

- [ ] **Step 1: 创建后端 pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.blog</groupId>
    <artifactId>personal-blog</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        
        <!-- Database -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.3</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.3</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.12.3</version>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <!-- Lombok (optional) -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 2: 创建前端 package.json**

```json
{
  "name": "personal-blog-frontend",
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "vue": "^3.4.21",
    "vue-router": "^4.3.0",
    "pinia": "^2.1.7",
    "axios": "^1.6.7",
    "highlight.js": "^11.9.0",
    "markdown-it": "^14.0.0"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.0.4",
    "vite": "^5.1.4",
    "tailwindcss": "^3.4.1",
    "postcss": "^8.4.35",
    "autoprefixer": "^10.4.17"
  }
}
```

- [ ] **Step 3: 创建 vite.config.js**

```javascript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  build: {
    outDir: '../backend/src/main/resources/static',
    emptyOutDir: true
  }
})
```

- [ ] **Step 4: 验证文件创建**

```bash
ls -la backend/pom.xml frontend/package.json frontend/vite.config.js
```

预期输出：三个文件都存在

- [ ] **Step 5: 估算耗时** - 25 min

---

## 阶段二：后端初始化

### 任务 2.1：创建 Spring Boot 主类

**Files:**
- Create: `backend/src/main/java/com/blog/BlogApplication.java`

- [ ] **Step 1: 创建主类**

```java
package com.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/BlogApplication.java
```

预期输出：正确的 Java 代码

- [ ] **Step 3: 估算耗时** - 5 min

---

### 任务 2.2：创建 application.yml 配置

**Files:**
- Create: `backend/src/main/resources/application.yml`

- [ ] **Step 1: 创建配置文件**

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: admin
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQLDialect

jwt:
  secret: personal-blog-jwt-secret-key-2026-must-be-at-least-32-characters-long
  expiration: 86400000

logging:
  level:
    com.blog: DEBUG
    org.springframework.security: INFO
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/resources/application.yml
```

预期输出：正确的 YAML 配置

- [ ] **Step 3: 估算耗时** - 10 min

---

### 任务 2.3：创建枚举类型

**Files:**
- Create: `backend/src/main/java/com/blog/entity/ArticleStatus.java`
- Create: `backend/src/main/java/com/blog/entity/Role.java`

- [ ] **Step 1: 创建 ArticleStatus 枚举**

```java
package com.blog.entity;

public enum ArticleStatus {
    DRAFT,
    PUBLISHED
}
```

- [ ] **Step 2: 创建 Role 枚举**

```java
package com.blog.entity;

public enum Role {
    ADMIN
}
```

- [ ] **Step 3: 验证文件**

```bash
cat backend/src/main/java/com/blog/entity/ArticleStatus.java
cat backend/src/main/java/com/blog/entity/Role.java
```

预期输出：两个枚举文件内容正确

- [ ] **Step 4: 估算耗时** - 10 min

---

### 任务 2.4：创建全局异常处理

**Files:**
- Create: `backend/src/main/java/com/blog/config/GlobalExceptionHandler.java`
- Create: `backend/src/main/java/com/blog/dto/ErrorResponse.java`

- [ ] **Step 1: 创建 ErrorResponse DTO**

```java
package com.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;
}
```

- [ ] **Step 2: 创建全局异常处理器**

```java
package com.blog.config;

import com.blog.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .message("Invalid username or password")
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .message(message)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .message("An unexpected error occurred")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
```

- [ ] **Step 3: 创建自定义异常**

```java
package com.blog.config;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

- [ ] **Step 4: 验证文件**

```bash
cat backend/src/main/java/com/blog/dto/ErrorResponse.java
cat backend/src/main/java/com/blog/config/GlobalExceptionHandler.java
cat backend/src/main/java/com/blog/config/ResourceNotFoundException.java
```

- [ ] **Step 5: 估算耗时** - 20 min

---

### 任务 2.5：创建 JWT 工具类

**Files:**
- Create: `backend/src/main/java/com/blog/security/JwtUtil.java`

- [ ] **Step 1: 创建 JWT 工具类**

```java
package com.blog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/security/JwtUtil.java
```

- [ ] **Step 3: 估算耗时** - 15 min

---

## 阶段三：数据库配置

### 任务 3.1：创建数据库初始化脚本

**Files:**
- Create: `backend/src/main/resources/db/migration/V1__create_tables.sql`

- [ ] **Step 1: 创建 SQL 脚本**

```sql
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    slug VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    slug VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    summary VARCHAR(500),
    cover_image VARCHAR(255),
    slug VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    view_count BIGINT DEFAULT 0,
    like_count BIGINT DEFAULT 0,
    category_id BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS article_tag (
    article_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (article_id, tag_id),
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    article_id BIGINT NOT NULL,
    author_name VARCHAR(100) NOT NULL,
    author_email VARCHAR(100),
    content TEXT NOT NULL,
    parent_id BIGINT,
    is_approved BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES comments(id) ON DELETE CASCADE
);

INSERT INTO users (username, password, email, role) VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 'admin@example.com', 'ADMIN');
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/resources/db/migration/V1__create_tables.sql
```

- [ ] **Step 3: 估算耗时** - 10 min

---

### 任务 3.2：配置 JPA 迁移

**Files:**
- Modify: `backend/src/main/resources/application.yml`

- [ ] **Step 1: 更新配置文件添加 Flyway**

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: admin
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQLDialect
  
  flyway:
    enabled: true
    baseline-on-migrate: true

jwt:
  secret: personal-blog-jwt-secret-key-2026-must-be-at-least-32-characters-long
  expiration: 86400000

logging:
  level:
    com.blog: DEBUG
    org.springframework.security: INFO
```

- [ ] **Step 2: 添加 Flyway 依赖到 pom.xml**

```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

- [ ] **Step 3: 验证配置**

```bash
grep -A 5 "flyway" backend/src/main/resources/application.yml
grep -A 3 "flyway" backend/pom.xml
```

- [ ] **Step 4: 估算耗时** - 5 min

---

### 任务 3.3：创建数据库连接测试

**Files:**
- Create: `backend/src/test/java/com/blog/DatabaseConnectionTest.java`

- [ ] **Step 1: 创建测试类**

```java
package com.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testDatabaseConnection() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertNotNull(result);
        assert result == 1;
    }
}
```

- [ ] **Step 2: 运行测试**

```bash
cd backend
mvn test -Dtest=DatabaseConnectionTest
```

预期输出：测试通过

- [ ] **Step 3: 估算耗时** - 5 min

---

## 阶段四：实体类创建

### 任务 4.1：创建 User 实体

**Files:**
- Create: `backend/src/main/java/com/blog/entity/User.java`

- [ ] **Step 1: 创建实体类**

```java
package com.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(length = 100)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.ADMIN;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/entity/User.java
```

- [ ] **Step 3: 估算耗时** - 5 min

---

### 任务 4.2：创建 Category 实体

**Files:**
- Create: `backend/src/main/java/com/blog/entity/Category.java`

- [ ] **Step 1: 创建实体类**

```java
package com.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String name;
    
    @Column(unique = true, nullable = false, length = 50)
    private String slug;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Article> articles = new ArrayList<>();
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/entity/Category.java
```

- [ ] **Step 3: 估算耗时** - 5 min

---

### 任务 4.3：创建 Tag 实体

**Files:**
- Create: `backend/src/main/java/com/blog/entity/Tag.java`

- [ ] **Step 1: 创建实体类**

```java
package com.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String name;
    
    @Column(unique = true, nullable = false, length = 50)
    private String slug;
    
    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<Article> articles = new HashSet<>();
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/entity/Tag.java
```

- [ ] **Step 3: 估算耗时** - 5 min

---

### 任务 4.4：创建 Article 实体

**Files:**
- Create: `backend/src/main/java/com/blog/entity/Article.java`

- [ ] **Step 1: 创建实体类**

```java
package com.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "articles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Column(length = 500)
    private String summary;
    
    @Column(name = "cover_image", length = 255)
    private String coverImage;
    
    @Column(unique = true, nullable = false)
    private String slug;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ArticleStatus status = ArticleStatus.DRAFT;
    
    @Column(name = "view_count")
    @Builder.Default
    private Long viewCount = 0L;
    
    @Column(name = "like_count")
    @Builder.Default
    private Long likeCount = 0L;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "article_tag",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
    
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/entity/Article.java
```

- [ ] **Step 3: 估算耗时** - 10 min

---

### 任务 4.5：创建 Comment 实体

**Files:**
- Create: `backend/src/main/java/com/blog/entity/Comment.java`

- [ ] **Step 1: 创建实体类**

```java
package com.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
    
    @Column(name = "author_name", nullable = false, length = 100)
    private String authorName;
    
    @Column(name = "author_email", length = 100)
    private String authorEmail;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Column(name = "parent_id")
    private Long parentId;
    
    @Column(name = "is_approved", nullable = false)
    @Builder.Default
    private Boolean isApproved = false;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/entity/Comment.java
```

- [ ] **Step 3: 估算耗时** - 5 min

---

## 阶段五：Repository 层

### 任务 5.1：创建 UserRepository

**Files:**
- Create: `backend/src/main/java/com/blog/repository/UserRepository.java`

- [ ] **Step 1: 创建 Repository**

```java
package com.blog.repository;

import com.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/repository/UserRepository.java
```

- [ ] **Step 3: 估算耗时** - 5 min

---

### 任务 5.2：创建 CategoryRepository

**Files:**
- Create: `backend/src/main/java/com/blog/repository/CategoryRepository.java`

- [ ] **Step 1: 创建 Repository**

```java
package com.blog.repository;

import com.blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/repository/CategoryRepository.java
```

- [ ] **Step 3: 估算耗时** - 5 min

---

### 任务 5.3：创建 TagRepository

**Files:**
- Create: `backend/src/main/java/com/blog/repository/TagRepository.java`

- [ ] **Step 1: 创建 Repository**

```java
package com.blog.repository;

import com.blog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findBySlug(String slug);
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/repository/TagRepository.java
```

- [ ] **Step 3: 估算耗时** - 5 min

---

### 任务 5.4：创建 ArticleRepository

**Files:**
- Create: `backend/src/main/java/com/blog/repository/ArticleRepository.java`

- [ ] **Step 1: 创建 Repository**

```java
package com.blog.repository;

import com.blog.entity.Article;
import com.blog.entity.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findBySlug(String slug);
    Page<Article> findByStatus(ArticleStatus status, Pageable pageable);
    Page<Article> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Article> findByTagsId(Long tagId, Pageable pageable);
    Page<Article> findByStatusAndCategoryId(ArticleStatus status, Long categoryId, Pageable pageable);
    Page<Article> findByStatusAndTagsId(ArticleStatus status, Long tagId, Pageable pageable);
    
    @Query("SELECT a FROM Article a WHERE a.status = :status AND (a.title LIKE %:keyword% OR a.summary LIKE %:keyword%)")
    Page<Article> searchByKeyword(@Param("keyword") String keyword, @Param("status") ArticleStatus status, Pageable pageable);
    
    @Query("SELECT a FROM Article a WHERE a.status = :status ORDER BY a.createdAt DESC")
    List<Article> findTopByStatusOrderByCreatedAtDesc(@Param("status") ArticleStatus status, Pageable pageable);
    
    @Modifying
    @Query("UPDATE Article a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    void incrementViewCount(@Param("id") Long id);
    
    @Modifying
    @Query("UPDATE Article a SET a.likeCount = a.likeCount + 1 WHERE a.id = :id")
    void incrementLikeCount(@Param("id") Long id);
    
    boolean existsBySlug(String slug);
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/repository/ArticleRepository.java
```

- [ ] **Step 3: 估算耗时** - 7 min

---

### 任务 5.5：创建 CommentRepository

**Files:**
- Create: `backend/src/main/java/com/blog/repository/CommentRepository.java`

- [ ] **Step 1: 创建 Repository**

```java
package com.blog.repository;

import com.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleIdAndIsApprovedTrueOrderByCreatedAtAsc(Long articleId);
    List<Comment> findByArticleIdOrderByCreatedAtAsc(Long articleId);
    List<Comment> findByParentId(Long parentId);
    long countByArticleId(Long articleId);
    long countByArticleIdAndIsApprovedTrue(Long articleId);
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/repository/CommentRepository.java
```

- [ ] **Step 3: 估算耗时** - 3 min

---

## 阶段六：DTO 层

### 任务 6.1：创建请求 DTO

**Files:**
- Create: `backend/src/main/java/com/blog/dto/request/ArticleCreateRequest.java`
- Create: `backend/src/main/java/com/blog/dto/request/ArticleUpdateRequest.java`
- Create: `backend/src/main/java/com/blog/dto/request/CategoryRequest.java`
- Create: `backend/src/main/java/com/blog/dto/request/TagRequest.java`
- Create: `backend/src/main/java/com/blog/dto/request/CommentRequest.java`
- Create: `backend/src/main/java/com/blog/dto/request/LoginRequest.java`

- [ ] **Step 1: 创建 ArticleCreateRequest**

```java
package com.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreateRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    @Size(max = 500, message = "Summary must be less than 500 characters")
    private String summary;
    
    private String coverImage;
    
    private Long categoryId;
    
    private List<Long> tagIds;
    
    @Builder.Default
    private String status = "DRAFT";
}
```

- [ ] **Step 2: 创建 ArticleUpdateRequest**

```java
package com.blog.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUpdateRequest {
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    
    private String content;
    
    @Size(max = 500, message = "Summary must be less than 500 characters")
    private String summary;
    
    private String coverImage;
    
    private Long categoryId;
    
    private List<Long> tagIds;
    
    private String status;
}
```

- [ ] **Step 3: 创建 CategoryRequest**

```java
package com.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;
    
    @Size(max = 50, message = "Slug must be less than 50 characters")
    private String slug;
    
    private String description;
}
```

- [ ] **Step 4: 创建 TagRequest**

```java
package com.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;
    
    @Size(max = 50, message = "Slug must be less than 50 characters")
    private String slug;
}
```

- [ ] **Step 5: 创建 CommentRequest**

```java
package com.blog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Author name is required")
    private String authorName;
    
    @Email(message = "Invalid email format")
    private String authorEmail;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    private Long parentId;
}
```

- [ ] **Step 6: 创建 LoginRequest**

```java
package com.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
}
```

- [ ] **Step 7: 验证所有文件**

```bash
ls -la backend/src/main/java/com/blog/dto/request/
```

- [ ] **Step 8: 估算耗时** - 15 min

---

### 任务 6.2：创建响应 DTO

**Files:**
- Create: `backend/src/main/java/com/blog/dto/response/ArticleResponse.java`
- Create: `backend/src/main/java/com/blog/dto/response/CommentResponse.java`
- Create: `backend/src/main/java/com/blog/dto/response/JwtResponse.java`

- [ ] **Step 1: 创建 ArticleResponse**

```java
package com.blog.dto.response;

import com.blog.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String coverImage;
    private String slug;
    private String status;
    private Long viewCount;
    private Long likeCount;
    private CategoryResponse category;
    private List<TagResponse> tags;
    private Long commentCount;
    private String createdAt;
    private String updatedAt;
    
    public static ArticleResponse fromEntity(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .summary(article.getSummary())
                .coverImage(article.getCoverImage())
                .slug(article.getSlug())
                .status(article.getStatus().name())
                .viewCount(article.getViewCount())
                .likeCount(article.getLikeCount())
                .category(article.getCategory() != null ? CategoryResponse.fromEntity(article.getCategory()) : null)
                .tags(article.getTags() != null ? 
                        article.getTags().stream().map(TagResponse::fromEntity).collect(Collectors.toList()) : null)
                .commentCount(article.getComments() != null ? (long) article.getComments().size() : 0L)
                .createdAt(article.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .updatedAt(article.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }
    
    public static ArticleResponse fromEntityWithCommentCount(Article article, Long commentCount) {
        ArticleResponse response = fromEntity(article);
        response.setCommentCount(commentCount);
        return response;
    }
}
```

- [ ] **Step 2: 创建 CategoryResponse**

```java
package com.blog.dto.response;

import com.blog.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private String description;
    
    public static CategoryResponse fromEntity(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .build();
    }
}
```

- [ ] **Step 3: 创建 TagResponse**

```java
package com.blog.dto.response;

import com.blog.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {
    private Long id;
    private String name;
    private String slug;
    
    public static TagResponse fromEntity(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .slug(tag.getSlug())
                .build();
    }
}
```

- [ ] **Step 4: 创建 CommentResponse**

```java
package com.blog.dto.response;

import com.blog.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private Long articleId;
    private String authorName;
    private String authorEmail;
    private String content;
    private Long parentId;
    private Boolean isApproved;
    private String createdAt;
    
    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .articleId(comment.getArticle().getId())
                .authorName(comment.getAuthorName())
                .authorEmail(comment.getAuthorEmail())
                .content(comment.getContent())
                .parentId(comment.getParentId())
                .isApproved(comment.getIsApproved())
                .createdAt(comment.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }
}
```

- [ ] **Step 5: 创建 JwtResponse**

```java
package com.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String role;
}
```

- [ ] **Step 6: 验证所有文件**

```bash
ls -la backend/src/main/java/com/blog/dto/response/
```

- [ ] **Step 7: 估算耗时** - 15 min

---

## 阶段七：Service 层

### 任务 7.1：创建 UserService

**Files:**
- Create: `backend/src/main/java/com/blog/service/UserService.java`
- Create: `backend/src/main/java/com/blog/service/impl/UserServiceImpl.java`

- [ ] **Step 1: 创建接口**

```java
package com.blog.service;

import com.blog.dto.response.JwtResponse;
import com.blog.dto.request.LoginRequest;

public interface UserService {
    JwtResponse login(LoginRequest loginRequest);
}
```

- [ ] **Step 2: 创建实现类**

```java
package com.blog.service.impl;

import com.blog.dto.response.JwtResponse;
import com.blog.dto.request.LoginRequest;
import com.blog.entity.User;
import com.blog.repository.UserRepository;
import com.blog.security.JwtUtil;
import com.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        
        UserDetails userDetails = loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        
        return JwtResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}
```

- [ ] **Step 3: 验证文件**

```bash
cat backend/src/main/java/com/blog/service/UserService.java
cat backend/src/main/java/com/blog/service/impl/UserServiceImpl.java
```

- [ ] **Step 4: 估算耗时** - 15 min

---

### 任务 7.2：创建 CategoryService

**Files:**
- Create: `backend/src/main/java/com/blog/service/CategoryService.java`
- Create: `backend/src/main/java/com/blog/service/impl/CategoryServiceImpl.java`

- [ ] **Step 1: 创建接口**

```java
package com.blog.service;

import com.blog.dto.request.CategoryRequest;
import com.blog.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}
```

- [ ] **Step 2: 创建实现类**

```java
package com.blog.service.impl;

import com.blog.config.ResourceNotFoundException;
import com.blog.dto.request.CategoryRequest;
import com.blog.dto.response.CategoryResponse;
import com.blog.entity.Category;
import com.blog.repository.CategoryRepository;
import com.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return CategoryResponse.fromEntity(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }
        
        String slug = StringUtils.hasText(request.getSlug()) 
                ? request.getSlug() 
                : request.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-");
        
        if (categoryRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Category slug already exists");
        }
        
        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .build();
        
        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        
        if (StringUtils.hasText(request.getName()) && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Category name already exists");
            }
            category.setName(request.getName());
        }
        
        if (StringUtils.hasText(request.getSlug())) {
            if (!request.getSlug().equals(category.getSlug()) && categoryRepository.existsBySlug(request.getSlug())) {
                throw new IllegalArgumentException("Category slug already exists");
            }
            category.setSlug(request.getSlug());
        }
        
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        
        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
```

- [ ] **Step 3: 验证文件**

```bash
cat backend/src/main/java/com/blog/service/CategoryService.java
cat backend/src/main/java/com/blog/service/impl/CategoryServiceImpl.java
```

- [ ] **Step 4: 估算耗时** - 12 min

---

### 任务 7.3：创建 TagService

**Files:**
- Create: `backend/src/main/java/com/blog/service/TagService.java`
- Create: `backend/src/main/java/com/blog/service/impl/TagServiceImpl.java`

- [ ] **Step 1: 创建接口**

```java
package com.blog.service;

import com.blog.dto.request.TagRequest;
import com.blog.dto.response.TagResponse;

import java.util.List;

public interface TagService {
    List<TagResponse> getAllTags();
    TagResponse getTagById(Long id);
    TagResponse createTag(TagRequest request);
    TagResponse updateTag(Long id, TagRequest request);
    void deleteTag(Long id);
}
```

- [ ] **Step 2: 创建实现类**

```java
package com.blog.service.impl;

import com.blog.config.ResourceNotFoundException;
import com.blog.dto.request.TagRequest;
import com.blog.dto.response.TagResponse;
import com.blog.entity.Tag;
import com.blog.repository.TagRepository;
import com.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(TagResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponse getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        return TagResponse.fromEntity(tag);
    }

    @Override
    @Transactional
    public TagResponse createTag(TagRequest request) {
        if (tagRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Tag name already exists");
        }
        
        String slug = StringUtils.hasText(request.getSlug()) 
                ? request.getSlug() 
                : request.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-");
        
        if (tagRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Tag slug already exists");
        }
        
        Tag tag = Tag.builder()
                .name(request.getName())
                .slug(slug)
                .build();
        
        return TagResponse.fromEntity(tagRepository.save(tag));
    }

    @Override
    @Transactional
    public TagResponse updateTag(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        
        if (StringUtils.hasText(request.getName()) && !request.getName().equals(tag.getName())) {
            if (tagRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Tag name already exists");
            }
            tag.setName(request.getName());
        }
        
        if (StringUtils.hasText(request.getSlug())) {
            if (!request.getSlug().equals(tag.getSlug()) && tagRepository.existsBySlug(request.getSlug())) {
                throw new IllegalArgumentException("Tag slug already exists");
            }
            tag.setSlug(request.getSlug());
        }
        
        return TagResponse.fromEntity(tagRepository.save(tag));
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }
}
```

- [ ] **Step 3: 验证文件**

```bash
cat backend/src/main/java/com/blog/service/TagService.java
cat backend/src/main/java/com/blog/service/impl/TagServiceImpl.java
```

- [ ] **Step 4: 估算耗时** - 12 min

---

### 任务 7.4：创建 ArticleService

**Files:**
- Create: `backend/src/main/java/com/blog/service/ArticleService.java`
- Create: `backend/src/main/java/com/blog/service/impl/ArticleServiceImpl.java`

- [ ] **Step 1: 创建接口**

```java
package com.blog.service;

import com.blog.dto.request.ArticleCreateRequest;
import com.blog.dto.request.ArticleUpdateRequest;
import com.blog.dto.response.ArticleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    Page<ArticleResponse> getAllArticles(Pageable pageable);
    Page<ArticleResponse> getPublishedArticles(Pageable pageable);
    ArticleResponse getArticleById(Long id);
    ArticleResponse getArticleBySlug(String slug);
    ArticleResponse createArticle(ArticleCreateRequest request);
    ArticleResponse updateArticle(Long id, ArticleUpdateRequest request);
    void deleteArticle(Long id);
    Page<ArticleResponse> searchArticles(String keyword, Pageable pageable);
    Page<ArticleResponse> getArticlesByCategory(Long categoryId, Pageable pageable);
    Page<ArticleResponse> getArticlesByTag(Long tagId, Pageable pageable);
    void incrementViewCount(Long id);
}
```

- [ ] **Step 2: 创建实现类**

```java
package com.blog.service.impl;

import com.blog.config.ResourceNotFoundException;
import com.blog.dto.request.ArticleCreateRequest;
import com.blog.dto.request.ArticleUpdateRequest;
import com.blog.dto.response.ArticleResponse;
import com.blog.entity.Article;
import com.blog.entity.ArticleStatus;
import com.blog.entity.Category;
import com.blog.entity.Tag;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CategoryRepository;
import com.blog.repository.CommentRepository;
import com.blog.repository.TagRepository;
import com.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    @Override
    public Page<ArticleResponse> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(article -> 
            ArticleResponse.fromEntityWithCommentCount(article, 
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId())));
    }

    @Override
    public Page<ArticleResponse> getPublishedArticles(Pageable pageable) {
        return articleRepository.findByStatus(ArticleStatus.PUBLISHED, pageable).map(article ->
            ArticleResponse.fromEntityWithCommentCount(article,
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId())));
    }

    @Override
    public ArticleResponse getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + id));
        return ArticleResponse.fromEntityWithCommentCount(article,
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId()));
    }

    @Override
    @Transactional
    public ArticleResponse getArticleBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with slug: " + slug));
        
        articleRepository.incrementViewCount(article.getId());
        
        return ArticleResponse.fromEntityWithCommentCount(article,
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId()));
    }

    @Override
    @Transactional
    public ArticleResponse createArticle(ArticleCreateRequest request) {
        String slug = generateSlug(request.getTitle());
        
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }
        
        Set<Tag> tags = new HashSet<>();
        if (request.getTagIds() != null) {
            request.getTagIds().forEach(tagId -> {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagId));
                tags.add(tag);
            });
        }
        
        ArticleStatus status = ArticleStatus.valueOf(request.getStatus().toUpperCase());
        
        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .summary(request.getSummary())
                .coverImage(request.getCoverImage())
                .slug(slug)
                .status(status)
                .category(category)
                .tags(tags)
                .build();
        
        return ArticleResponse.fromEntity(articleRepository.save(article));
    }

    @Override
    @Transactional
    public ArticleResponse updateArticle(Long id, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + id));
        
        if (StringUtils.hasText(request.getTitle())) {
            article.setTitle(request.getTitle());
            article.setSlug(generateSlug(request.getTitle()));
        }
        
        if (request.getContent() != null) {
            article.setContent(request.getContent());
        }
        
        if (request.getSummary() != null) {
            article.setSummary(request.getSummary());
        }
        
        if (request.getCoverImage() != null) {
            article.setCoverImage(request.getCoverImage());
        }
        
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            article.setCategory(category);
        }
        
        if (request.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>();
            request.getTagIds().forEach(tagId -> {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagId));
                tags.add(tag);
            });
            article.setTags(tags);
        }
        
        if (StringUtils.hasText(request.getStatus())) {
            article.setStatus(ArticleStatus.valueOf(request.getStatus().toUpperCase()));
        }
        
        return ArticleResponse.fromEntity(articleRepository.save(article));
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Article not found with id: " + id);
        }
        articleRepository.deleteById(id);
    }

    @Override
    public Page<ArticleResponse> searchArticles(String keyword, Pageable pageable) {
        return articleRepository.searchByKeyword(keyword, ArticleStatus.PUBLISHED, pageable).map(article ->
            ArticleResponse.fromEntityWithCommentCount(article,
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId())));
    }

    @Override
    public Page<ArticleResponse> getArticlesByCategory(Long categoryId, Pageable pageable) {
        return articleRepository.findByStatusAndCategoryId(ArticleStatus.PUBLISHED, categoryId, pageable).map(article ->
            ArticleResponse.fromEntityWithCommentCount(article,
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId())));
    }

    @Override
    public Page<ArticleResponse> getArticlesByTag(Long tagId, Pageable pageable) {
        return articleRepository.findByStatusAndTagsId(ArticleStatus.PUBLISHED, tagId, pageable).map(article ->
            ArticleResponse.fromEntityWithCommentCount(article,
                commentRepository.countByArticleIdAndIsApprovedTrue(article.getId())));
    }

    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        articleRepository.incrementViewCount(id);
    }

    private String generateSlug(String title) {
        String slug = title.toLowerCase().replaceAll("[^a-z0-9\\s]+", "").replaceAll("\\s+", "-");
        int counter = 1;
        String originalSlug = slug;
        
        while (articleRepository.existsBySlug(slug)) {
            slug = originalSlug + "-" + counter++;
        }
        
        return slug;
    }
}
```

- [ ] **Step 3: 验证文件**

```bash
cat backend/src/main/java/com/blog/service/ArticleService.java
cat backend/src/main/java/com/blog/service/impl/ArticleServiceImpl.java
```

- [ ] **Step 4: 估算耗时** - 21 min

---

### 任务 7.5：创建 CommentService

**Files:**
- Create: `backend/src/main/java/com/blog/service/CommentService.java`
- Create: `backend/src/main/java/com/blog/service/impl/CommentServiceImpl.java`

- [ ] **Step 1: 创建接口**

```java
package com.blog.service;

import com.blog.dto.request.CommentRequest;
import com.blog.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getCommentsByArticle(Long articleId);
    List<CommentResponse> getAllComments();
    CommentResponse createComment(Long articleId, CommentRequest request);
    CommentResponse approveComment(Long id);
    void deleteComment(Long id);
}
```

- [ ] **Step 2: 创建实现类**

```java
package com.blog.service.impl;

import com.blog.config.ResourceNotFoundException;
import com.blog.dto.request.CommentRequest;
import com.blog.dto.response.CommentResponse;
import com.blog.entity.Article;
import com.blog.entity.Comment;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CommentRepository;
import com.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Override
    public List<CommentResponse> getCommentsByArticle(Long articleId) {
        return commentRepository.findByArticleIdAndIsApprovedTrueOrderByCreatedAtAsc(articleId).stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll().stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentResponse createComment(Long articleId, CommentRequest request) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        
        Comment comment = Comment.builder()
                .article(article)
                .authorName(request.getAuthorName())
                .authorEmail(request.getAuthorEmail())
                .content(request.getContent())
                .parentId(request.getParentId())
                .isApproved(false)
                .build();
        
        return CommentResponse.fromEntity(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentResponse approveComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        comment.setIsApproved(true);
        return CommentResponse.fromEntity(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }
}
```

- [ ] **Step 3: 验证文件**

```bash
cat backend/src/main/java/com/blog/service/CommentService.java
cat backend/src/main/java/com/blog/service/impl/CommentServiceImpl.java
```

- [ ] **Step 4: 估算耗时** - 10 min

---

## 阶段八：Controller 层

### 任务 8.1：创建 AuthController

**Files:**
- Create: `backend/src/main/java/com/blog/controller/AuthController.java`

- [ ] **Step 1: 创建控制器**

```java
package com.blog.controller;

import com.blog.dto.response.JwtResponse;
import com.blog.dto.request.LoginRequest;
import com.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/controller/AuthController.java
```

- [ ] **Step 3: 估算耗时** - 5 min

---

### 任务 8.2：创建 ArticleController

**Files:**
- Create: `backend/src/main/java/com/blog/controller/ArticleController.java`

- [ ] **Step 1: 创建控制器**

```java
package com.blog.controller;

import com.blog.dto.request.ArticleCreateRequest;
import com.blog.dto.request.ArticleUpdateRequest;
import com.blog.dto.response.ArticleResponse;
import com.blog.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public ResponseEntity<Page<ArticleResponse>> getArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<ArticleResponse> articles;
        if (categoryId != null) {
            articles = articleService.getArticlesByCategory(categoryId, pageable);
        } else if (tagId != null) {
            articles = articleService.getArticlesByTag(tagId, pageable);
        } else {
            articles = articleService.getPublishedArticles(pageable);
        }
        
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }

    @GetMapping("/articles/slug/{slug}")
    public ResponseEntity<ArticleResponse> getArticleBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(articleService.getArticleBySlug(slug));
    }

    @GetMapping("/articles/search")
    public ResponseEntity<Page<ArticleResponse>> searchArticles(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(articleService.searchArticles(q, pageable));
    }

    @PostMapping("/admin/articles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArticleResponse> createArticle(@Valid @RequestBody ArticleCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(request));
    }

    @PutMapping("/admin/articles/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleUpdateRequest request) {
        return ResponseEntity.ok(articleService.updateArticle(id, request));
    }

    @DeleteMapping("/admin/articles/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/controller/ArticleController.java
```

- [ ] **Step 3: 估算耗时** - 10 min

---

### 任务 8.3：创建 CategoryController

**Files:**
- Create: `backend/src/main/java/com/blog/controller/CategoryController.java`

- [ ] **Step 1: 创建控制器**

```java
package com.blog.controller;

import com.blog.dto.request.CategoryRequest;
import com.blog.dto.response.CategoryResponse;
import com.blog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping("/admin/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }

    @PutMapping("/admin/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/admin/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/controller/CategoryController.java
```

- [ ] **Step 3: 估算耗时** - 8 min

---

### 任务 8.4：创建 TagController

**Files:**
- Create: `backend/src/main/java/com/blog/controller/TagController.java`

- [ ] **Step 1: 创建控制器**

```java
package com.blog.controller;

import com.blog.dto.request.TagRequest;
import com.blog.dto.response.TagResponse;
import com.blog.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/tags")
    public ResponseEntity<List<TagResponse>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<TagResponse> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PostMapping("/admin/tags")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.createTag(request));
    }

    @PutMapping("/admin/tags/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagResponse> updateTag(
            @PathVariable Long id,
            @RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.updateTag(id, request));
    }

    @DeleteMapping("/admin/tags/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/controller/TagController.java
```

- [ ] **Step 3: 估算耗时** - 8 min

---

### 任务 8.5：创建 CommentController

**Files:**
- Create: `backend/src/main/java/com/blog/controller/CommentController.java`

- [ ] **Step 1: 创建控制器**

```java
package com.blog.controller;

import com.blog.dto.request.CommentRequest;
import com.blog.dto.response.CommentResponse;
import com.blog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/articles/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByArticle(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsByArticle(id));
    }

    @GetMapping("/admin/comments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @PostMapping("/articles/{id}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(id, request));
    }

    @PutMapping("/admin/comments/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommentResponse> approveComment(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.approveComment(id));
    }

    @DeleteMapping("/admin/comments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/controller/CommentController.java
```

- [ ] **Step 3: 估算耗时** - 8 min

---

### 任务 8.6：创建通用控制器（健康检查等）

**Files:**
- Create: `backend/src/main/java/com/blog/controller/HealthController.java`

- [ ] **Step 1: 创建控制器**

```java
package com.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Personal Blog API");
        return ResponseEntity.ok(response);
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/controller/HealthController.java
```

- [ ] **Step 3: 估算耗时** - 4 min

---

## 阶段九：安全配置

### 任务 9.1：创建 SecurityConfig

**Files:**
- Create: `backend/src/main/java/com/blog/config/SecurityConfig.java`

- [ ] **Step 1: 创建配置类**

```java
package com.blog.config;

import com.blog.security.JwtAuthenticationFilter;
import com.blog.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserServiceImpl userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/api/health/**", "/api/articles/**", "/api/categories/**", "/api/tags/**", "/articles/**")
                .permitAll()
                .requestMatchers("/api/admin/**")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/config/SecurityConfig.java
```

- [ ] **Step 3: 估算耗时** - 12 min

---

### 任务 9.2：创建 JwtAuthenticationFilter

**Files:**
- Create: `backend/src/main/java/com/blog/security/JwtAuthenticationFilter.java`

- [ ] **Step 1: 创建过滤器**

```java
package com.blog.security;

import com.blog.service.impl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserServiceImpl userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        jwt = authHeader.substring(7);
        username = jwtUtil.extractUsername(jwt);
        
        if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/security/JwtAuthenticationFilter.java
```

- [ ] **Step 3: 估算耗时** - 10 min

---

### 任务 9.3：配置 WebMvcConfig（前端路由支持）

**Files:**
- Create: `backend/src/main/java/com/blog/config/WebMvcConfig.java`

- [ ] **Step 1: 创建配置类**

```java
package com.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{path:[^\\.]*}")
                .setViewName("forward:/index.html");
        
        registry.addViewController("/**/{path:[^\\.]*}")
                .setViewName("forward:/index.html");
    }
}
```

- [ ] **Step 2: 验证文件**

```bash
cat backend/src/main/java/com/blog/config/WebMvcConfig.java
```

- [ ] **Step 3: 估算耗时** - 8 min

---

## 阶段十：前端初始化

### 任务 10.1：创建前端入口文件

**Files:**
- Create: `frontend/src/main.js`
- Create: `frontend/src/App.vue`
- Create: `frontend/index.html`

- [ ] **Step 1: 创建 main.js**

```javascript
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'
import './style.css'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.mount('#app')
```

- [ ] **Step 2: 创建 App.vue**

```vue
<template>
  <router-view />
</template>

<script setup>
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  line-height: 1.6;
  color: #333;
  background-color: #f8f9fa;
}
</style>
```

- [ ] **Step 3: 创建 index.html**

```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Personal Blog</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>
  <div id="app"></div>
  <script type="module" src="/src/main.js"></script>
</body>
</html>
```

- [ ] **Step 4: 创建 style.css**

```css
@import "tailwindcss";

@theme {
  --color-primary: #3b82f6;
  --color-primary-dark: #2563eb;
  --color-secondary: #6b7280;
  --color-success: #10b981;
  --color-danger: #ef4444;
  --color-bg: #f8f9fa;
  --color-card: #ffffff;
  --color-text: #1f2937;
  --color-text-secondary: #6b7280;
  --border-color: #e5e7eb;
  --border-radius: 0.5rem;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

.card {
  background: var(--color-card);
  border-radius: var(--border-radius);
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px -1px rgba(0, 0, 0, 0.1);
  padding: 1.5rem;
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem 1rem;
  border-radius: var(--border-radius);
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
  text-decoration: none;
}

.btn-primary {
  background-color: var(--color-primary);
  color: white;
}

.btn-primary:hover {
  background-color: var(--color-primary-dark);
}

.btn-secondary {
  background-color: var(--color-secondary);
  color: white;
}

.btn-danger {
  background-color: var(--color-danger);
  color: white;
}

.form-group {
  margin-bottom: 1rem;
}

.form-label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-control {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  font-size: 1rem;
}

.form-control:focus {
  outline: none;
  border-color: var(--color-primary);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
}

.text-danger {
  color: var(--color-danger);
  font-size: 0.875rem;
}

.text-success {
  color: var(--color-success);
}

.mt-1 { margin-top: 0.25rem; }
.mt-2 { margin-top: 0.5rem; }
.mt-4 { margin-top: 1rem; }
.mb-2 { margin-bottom: 0.5rem; }
.mb-4 { margin-bottom: 1rem; }
.p-4 { padding: 1rem; }
```

- [ ] **Step 5: 验证文件**

```bash
ls -la frontend/src/main.js frontend/src/App.vue frontend/index.html frontend/src/style.css
```

- [ ] **Step 6: 估算耗时** - 10 min

---

### 任务 10.2：创建路由配置

**Files:**
- Create: `frontend/src/router/index.js`

- [ ] **Step 1: 创建路由配置**

```javascript
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/HomeView.vue')
  },
  {
    path: '/article/:slug',
    name: 'ArticleDetail',
    component: () => import('../views/ArticleDetailView.vue')
  },
  {
    path: '/category/:id',
    name: 'Category',
    component: () => import('../views/CategoryView.vue')
  },
  {
    path: '/tag/:id',
    name: 'Tag',
    component: () => import('../views/TagView.vue')
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('../views/SearchView.vue')
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/AboutView.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/admin/AdminView.vue'),
    children: [
      {
        path: '',
        name: 'AdminDashboard',
        component: () => import('../views/admin/DashboardView.vue')
      },
      {
        path: 'articles',
        name: 'AdminArticles',
        component: () => import('../views/admin/ArticlesView.vue')
      },
      {
        path: 'articles/create',
        name: 'CreateArticle',
        component: () => import('../views/admin/CreateArticleView.vue')
      },
      {
        path: 'articles/edit/:id',
        name: 'EditArticle',
        component: () => import('../views/admin/EditArticleView.vue')
      },
      {
        path: 'categories',
        name: 'AdminCategories',
        component: () => import('../views/admin/CategoriesView.vue')
      },
      {
        path: 'tags',
        name: 'AdminTags',
        component: () => import('../views/admin/TagsView.vue')
      },
      {
        path: 'comments',
        name: 'AdminComments',
        component: () => import('../views/admin/CommentsView.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token')
  const isAdminRoute = to.path.startsWith('/admin')
  
  if (isAdminRoute && !isAuthenticated) {
    next('/login')
  } else if (to.path === '/login' && isAuthenticated) {
    next('/admin')
  } else {
    next()
  }
})

export default router
```

- [ ] **Step 2: 验证文件**

```bash
cat frontend/src/router/index.js
```

- [ ] **Step 3: 估算耗时** - 8 min

---

### 任务 10.3：创建 API 和 Store

**Files:**
- Create: `frontend/src/api/index.js`
- Create: `frontend/src/store/auth.js`
- Create: `frontend/src/store/articles.js`

- [ ] **Step 1: 创建 API 配置**

```javascript
import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const authApi = {
  login: (data) => api.post('/auth/login', data)
}

export const articleApi = {
  getAll: (params) => api.get('/articles', { params }),
  getById: (id) => api.get(`/articles/${id}`),
  getBySlug: (slug) => api.get(`/articles/slug/${slug}`),
  search: (params) => api.get('/articles/search', { params }),
  create: (data) => api.post('/admin/articles', data),
  update: (id, data) => api.put(`/admin/articles/${id}`, data),
  delete: (id) => api.delete(`/admin/articles/${id}`)
}

export const categoryApi = {
  getAll: () => api.get('/categories'),
  getById: (id) => api.get(`/categories/${id}`),
  create: (data) => api.post('/admin/categories', data),
  update: (id, data) => api.put(`/admin/categories/${id}`, data),
  delete: (id) => api.delete(`/admin/categories/${id}`)
}

export const tagApi = {
  getAll: () => api.get('/tags'),
  getById: (id) => api.get(`/tags/${id}`),
  create: (data) => api.post('/admin/tags', data),
  update: (id, data) => api.put(`/admin/tags/${id}`, data),
  delete: (id) => api.delete(`/admin/tags/${id}`)
}

export const commentApi = {
  getByArticle: (articleId) => api.get(`/articles/${articleId}/comments`),
  getAll: () => api.get('/admin/comments'),
  create: (articleId, data) => api.post(`/articles/${articleId}/comments`, data),
  approve: (id) => api.put(`/admin/comments/${id}/approve`),
  delete: (id) => api.delete(`/admin/comments/${id}`)
}

export default api
```

- [ ] **Step 2: 创建 auth store**

```javascript
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { authApi } from '../api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || null)
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))

  const login = async (username, password) => {
    try {
      const response = await authApi.login({ username, password })
      token.value = response.data.token
      user.value = {
        username: response.data.username,
        role: response.data.role
      }
      localStorage.setItem('token', token.value)
      localStorage.setItem('user', JSON.stringify(user.value))
      return response.data
    } catch (error) {
      throw error.response?.data?.message || 'Login failed'
    }
  }

  const logout = () => {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  const isAuthenticated = () => {
    return !!token.value
  }

  return {
    token,
    user,
    login,
    logout,
    isAuthenticated
  }
})
```

- [ ] **Step 3: 创建 articles store**

```javascript
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { articleApi, categoryApi, tagApi, commentApi } from '../api'

export const useArticlesStore = defineStore('articles', () => {
  const articles = ref([])
  const categories = ref([])
  const tags = ref([])
  const currentArticle = ref(null)
  const loading = ref(false)

  const fetchArticles = async (params = {}) => {
    loading.value = true
    try {
      const response = await articleApi.getAll(params)
      articles.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const fetchArticleBySlug = async (slug) => {
    loading.value = true
    try {
      const response = await articleApi.getBySlug(slug)
      currentArticle.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const fetchCategories = async () => {
    const response = await categoryApi.getAll()
    categories.value = response.data
    return response.data
  }

  const fetchTags = async () => {
    const response = await tagApi.getAll()
    tags.value = response.data
    return response.data
  }

  const searchArticles = async (keyword) => {
    loading.value = true
    try {
      const response = await articleApi.search({ q: keyword })
      articles.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const fetchArticlesByCategory = async (categoryId) => {
    loading.value = true
    try {
      const response = await articleApi.getAll({ categoryId })
      articles.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const fetchArticlesByTag = async (tagId) => {
    loading.value = true
    try {
      const response = await articleApi.getAll({ tagId })
      articles.value = response.data
      return response.data
    } finally {
      loading.value = false
    }
  }

  const createArticle = async (data) => {
    const response = await articleApi.create(data)
    return response.data
  }

  const updateArticle = async (id, data) => {
    const response = await articleApi.update(id, data)
    return response.data
  }

  const deleteArticle = async (id) => {
    await articleApi.delete(id)
  }

  const fetchComments = async (articleId) => {
    const response = await commentApi.getByArticle(articleId)
    return response.data
  }

  const createComment = async (articleId, data) => {
    const response = await commentApi.create(articleId, data)
    return response.data
  }

  return {
    articles,
    categories,
    tags,
    currentArticle,
    loading,
    fetchArticles,
    fetchArticleBySlug,
    fetchCategories,
    fetchTags,
    searchArticles,
    fetchArticlesByCategory,
    fetchArticlesByTag,
    createArticle,
    updateArticle,
    deleteArticle,
    fetchComments,
    createComment
  }
})
```

- [ ] **Step 4: 验证文件**

```bash
ls -la frontend/src/api/index.js frontend/src/store/auth.js frontend/src/store/articles.js
```

- [ ] **Step 5: 估算耗时** - 12 min

---

## 阶段十一：前端组件

### 任务 11.1：创建公共组件

**Files:**
- Create: `frontend/src/components/Header.vue`
- Create: `frontend/src/components/Footer.vue`
- Create: `frontend/src/components/ArticleCard.vue`

- [ ] **Step 1: 创建 Header 组件**

```vue
<template>
  <header class="bg-white shadow-sm sticky top-0 z-50">
    <div class="container">
      <div class="flex items-center justify-between h-16">
        <div class="flex items-center space-x-8">
          <router-link to="/" class="text-xl font-bold text-primary">
            <i class="fas fa-blog mr-2"></i>
            My Blog
          </router-link>
          <nav class="hidden md:flex space-x-6">
            <router-link 
              v-for="item in navItems" 
              :key="item.path"
              :to="item.path"
              class="text-secondary hover:text-primary transition-colors"
            >
              {{ item.label }}
            </router-link>
          </nav>
        </div>
        
        <div class="flex items-center space-x-4">
          <div class="relative hidden md:block">
            <input
              type="text"
              v-model="searchQuery"
              placeholder="搜索文章..."
              class="w-64 px-4 py-2 text-sm border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-primary/50"
              @keyup.enter="handleSearch"
            />
            <button 
              class="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-primary"
              @click="handleSearch"
            >
              <i class="fas fa-search"></i>
            </button>
          </div>
          
          <button 
            v-if="isAuthenticated"
            class="hidden md:flex items-center space-x-2 px-4 py-2 bg-primary text-white rounded-lg hover:bg-primary-dark transition-colors"
            @click="goToAdmin"
          >
            <i class="fas fa-cog"></i>
            <span>管理后台</span>
          </button>
          
          <button 
            v-else
            class="hidden md:flex items-center space-x-2 px-4 py-2 border border-primary text-primary rounded-lg hover:bg-primary hover:text-white transition-colors"
            @click="goToLogin"
          >
            <i class="fas fa-sign-in-alt"></i>
            <span>登录</span>
          </button>
          
          <button 
            class="md:hidden text-gray-600"
            @click="mobileMenuOpen = !mobileMenuOpen"
          >
            <i class="fas fa-bars"></i>
          </button>
        </div>
      </div>
      
      <div v-if="mobileMenuOpen" class="md:hidden py-4 border-t">
        <div class="space-y-2">
          <router-link 
            v-for="item in navItems" 
            :key="item.path"
            :to="item.path"
            class="block px-4 py-2 text-secondary hover:text-primary hover:bg-gray-50 rounded-lg"
            @click="mobileMenuOpen = false"
          >
            {{ item.label }}
          </router-link>
          <div class="px-4 pt-2 border-t mt-2">
            <input
              type="text"
              v-model="searchQuery"
              placeholder="搜索文章..."
              class="w-full px-4 py-2 text-sm border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-primary/50"
              @keyup.enter="handleSearch"
            />
          </div>
          <button 
            v-if="isAuthenticated"
            class="w-full mx-4 mt-2 px-4 py-2 bg-primary text-white rounded-lg"
            @click="goToAdmin"
          >
            管理后台
          </button>
          <button 
            v-else
            class="w-full mx-4 mt-2 px-4 py-2 border border-primary text-primary rounded-lg"
            @click="goToLogin"
          >
            登录
          </button>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'

const router = useRouter()
const authStore = useAuthStore()

const searchQuery = ref('')
const mobileMenuOpen = ref(false)

const navItems = [
  { label: '首页', path: '/' },
  { label: '分类', path: '#', action: 'showCategories' },
  { label: '标签', path: '#', action: 'showTags' },
  { label: '关于', path: '/about' }
]

const isAuthenticated = computed(() => authStore.isAuthenticated())

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({ path: '/search', query: { q: searchQuery.value } })
    searchQuery.value = ''
    mobileMenuOpen.value = false
  }
}

const goToAdmin = () => {
  router.push('/admin')
}

const goToLogin = () => {
  router.push('/login')
}
</script>
```

- [ ] **Step 2: 创建 Footer 组件**

```vue
<template>
  <footer class="bg-gray-800 text-white py-8 mt-12">
    <div class="container">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
        <div class="md:col-span-2">
          <h3 class="text-xl font-bold mb-4 flex items-center">
            <i class="fas fa-blog mr-2"></i>
            My Blog
          </h3>
          <p class="text-gray-400 mb-4">
            欢迎来到我的个人博客，这里记录了我的技术学习和生活感悟。
          </p>
          <div class="flex space-x-4">
            <a href="#" class="text-gray-400 hover:text-white transition-colors">
              <i class="fab fa-github text-xl"></i>
            </a>
            <a href="#" class="text-gray-400 hover:text-white transition-colors">
              <i class="fab fa-twitter text-xl"></i>
            </a>
            <a href="#" class="text-gray-400 hover:text-white transition-colors">
              <i class="fab fa-linkedin text-xl"></i>
            </a>
          </div>
        </div>
        
        <div>
          <h4 class="font-semibold mb-4">快速链接</h4>
          <ul class="space-y-2">
            <li>
              <router-link to="/" class="text-gray-400 hover:text-white transition-colors">
                首页
              </router-link>
            </li>
            <li>
              <router-link to="/about" class="text-gray-400 hover:text-white transition-colors">
                关于我
              </router-link>
            </li>
            <li>
              <router-link to="/admin" class="text-gray-400 hover:text-white transition-colors">
                管理后台
              </router-link>
            </li>
          </ul>
        </div>
        
        <div>
          <h4 class="font-semibold mb-4">联系我</h4>
          <ul class="space-y-2 text-gray-400">
            <li class="flex items-center">
              <i class="fas fa-envelope mr-2"></i>
              admin@example.com
            </li>
            <li class="flex items-center">
              <i class="fas fa-map-marker-alt mr-2"></i>
              中国
            </li>
          </ul>
        </div>
      </div>
      
      <div class="border-t border-gray-700 mt-8 pt-8 text-center text-gray-400">
        <p>&copy; {{ currentYear }} My Blog. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { computed } from 'vue'

const currentYear = computed(() => new Date().getFullYear())
</script>
```

- [ ] **Step 3: 创建 ArticleCard 组件**

```vue
<template>
  <div class="card hover:shadow-lg transition-shadow cursor-pointer" @click="goToArticle">
    <div v-if="article.coverImage" class="mb-4">
      <img :src="article.coverImage" :alt="article.title" class="w-full h-48 object-cover rounded-lg">
    </div>
    
    <div class="flex items-center space-x-2 mb-2">
      <span 
        v-if="article.category"
        class="px-2 py-1 bg-primary/10 text-primary text-xs rounded-full"
      >
        {{ article.category.name }}
      </span>
      <span class="text-gray-400 text-sm">{{ formatDate(article.createdAt) }}</span>
    </div>
    
    <h3 class="text-lg font-semibold text-text mb-2 line-clamp-2">{{ article.title }}</h3>
    
    <p class="text-gray-500 text-sm mb-4 line-clamp-2">{{ article.summary }}</p>
    
    <div class="flex items-center justify-between">
      <div class="flex flex-wrap gap-2">
        <span 
          v-for="tag in article.tags?.slice(0, 3)" 
          :key="tag.id"
          class="px-2 py-1 bg-gray-100 text-gray-600 text-xs rounded"
        >
          {{ tag.name }}
        </span>
        <span v-if="article.tags?.length > 3" class="text-gray-400 text-xs">
          +{{ article.tags.length - 3 }}
        </span>
      </div>
      
      <div class="flex items-center space-x-4 text-gray-400 text-sm">
        <span class="flex items-center">
          <i class="fas fa-eye mr-1"></i>
          {{ article.viewCount }}
        </span>
        <span class="flex items-center">
          <i class="fas fa-comment mr-1"></i>
          {{ article.commentCount }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const props = defineProps({
  article: {
    type: Object,
    required: true
  }
})

const goToArticle = () => {
  router.push(`/article/${props.article.slug}`)
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}
</script>
```

- [ ] **Step 4: 验证文件**

```bash
ls -la frontend/src/components/Header.vue frontend/src/components/Footer.vue frontend/src/components/ArticleCard.vue
```

- [ ] **Step 5: 估算耗时** - 15 min