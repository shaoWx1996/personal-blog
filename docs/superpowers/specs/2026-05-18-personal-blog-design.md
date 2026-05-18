# Personal Blog 设计文档

**日期**: 2026-05-18  
**主题**: 个人博客项目设计

---

## 1. 项目概述

### 1.1 项目背景
创建一个个人博客，用于分享技术文章和记录个人生活。

### 1.2 博客定位
- **类型**: 混合类型（技术 + 个人生活）
- **受众**: 技术开发者、朋友、关注者
- **风格**: 中性平衡，既专业又亲切

---

## 2. 技术架构

### 2.1 整体架构
采用混合架构（方案 C）：
- 后端：Spring Boot 提供 REST API
- 前端：Vue 3 SPA
- 部署：Vue 构建后放到 Spring Boot 的 `static` 目录

### 2.2 技术栈
| 层级 | 技术选型 |
|------|----------|
| 后端 | Java 17+, Spring Boot 3.x |
| ORM | Spring Data JPA / MyBatis |
| 数据库 | MySQL 8.0+ |
| 前端 | Vue 3 + Vite |
| 路由 | Vue Router |
| 状态管理 | Pinia |
| HTTP 客户端 | Axios |

### 2.3 项目结构
```
personal-blog/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/blog/
│   │   │   │   ├── controller/     # REST API 控制器
│   │   │   │   ├── service/        # 业务逻辑
│   │   │   │   ├── repository/     # 数据访问
│   │   │   │   ├── entity/         # JPA 实体
│   │   │   │   ├── dto/            # 数据传输对象
│   │   │   │   ├── config/         # 配置类
│   │   │   │   ├── security/       # 安全认证
│   │   │   │   └── BlogApplication.java
│   │   │   └── resources/
│   │   │       ├── static/         # Vue 构建产物
│   │   │       ├── application.yml
│   │   │       └── db/migration/   # 数据库迁移
│   │   └── test/
│   └── pom.xml
└── frontend/
    ├── src/
    │   ├── components/
    │   ├── views/
    │   ├── router/
    │   ├── store/
    │   ├── api/
    │   ├── App.vue
    │   └── main.js
    ├── package.json
    └── vite.config.js
```

---

## 3. 数据模型设计

### 3.1 核心实体

#### Article（文章）
```java
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;           // 标题
    @Column(columnDefinition = "TEXT")
    private String content;         // 内容（Markdown）
    private String summary;         // 摘要
    private String coverImage;      // 封面图
    private String slug;            // URL 友好标识
    
    @Enumerated(EnumType.STRING)
    private ArticleStatus status;   // DRAFT, PUBLISHED
    
    private Long viewCount;         // 浏览次数
    private Long likeCount;         // 点赞数
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;      // 分类
    
    @ManyToMany
    @JoinTable(
        name = "article_tag",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;          // 标签
    
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

#### Category（分类）
```java
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;            // 分类名称
    private String slug;            // URL 标识
    private String description;     // 描述
    
    @OneToMany(mappedBy = "category")
    private List<Article> articles;
}
```

#### Tag（标签）
```java
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;            // 标签名称
    private String slug;            // URL 标识
    
    @ManyToMany(mappedBy = "tags")
    private Set<Article> articles;
}
```

#### Comment（评论）
```java
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;
    
    private String authorName;      // 评论者名称
    private String authorEmail;     // 评论者邮箱
    @Column(columnDefinition = "TEXT")
    private String content;         // 评论内容
    
    private Long parentId;          // 回复的评论 ID（支持嵌套）
    
    private Boolean isApproved;     // 是否已审核
    private LocalDateTime createdAt;
}
```

#### User（用户）
```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password;        // BCrypt 加密
    private String email;
    
    @Enumerated(EnumType.STRING)
    private Role role;              // ADMIN
    
    private LocalDateTime createdAt;
}
```

---

## 4. API 设计

### 4.1 文章相关 API

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/articles` | 获取文章列表（分页、筛选） | 否 |
| GET | `/api/articles/{id}` | 获取单篇文章 | 否 |
| GET | `/api/articles/slug/{slug}` | 通过 slug 获取文章 | 否 |
| POST | `/api/admin/articles` | 创建文章 | 是 |
| PUT | `/api/admin/articles/{id}` | 更新文章 | 是 |
| DELETE | `/api/admin/articles/{id}` | 删除文章 | 是 |
| GET | `/api/articles/search` | 搜索文章 | 否 |

查询参数（列表）：
- `page`: 页码（默认 0）
- `size`: 每页数量（默认 10）
- `categoryId`: 按分类筛选
- `tagId`: 按标签筛选
- `status`: 按状态筛选（仅管理员）

### 4.2 分类 API

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/categories` | 获取所有分类 | 否 |
| GET | `/api/categories/{id}` | 获取单个分类 | 否 |
| POST | `/api/admin/categories` | 创建分类 | 是 |
| PUT | `/api/admin/categories/{id}` | 更新分类 | 是 |
| DELETE | `/api/admin/categories/{id}` | 删除分类 | 是 |

### 4.3 标签 API

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/tags` | 获取所有标签 | 否 |
| POST | `/api/admin/tags` | 创建标签 | 是 |
| PUT | `/api/admin/tags/{id}` | 更新标签 | 是 |
| DELETE | `/api/admin/tags/{id}` | 删除标签 | 是 |

### 4.4 评论 API

| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/api/articles/{id}/comments` | 获取文章评论 | 否 |
| POST | `/api/articles/{id}/comments` | 发表评论 | 否 |
| PUT | `/api/admin/comments/{id}/approve` | 审核通过评论 | 是 |
| DELETE | `/api/admin/comments/{id}` | 删除评论 | 是 |

### 4.5 认证 API

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/auth/login` | 管理员登录 |

---

## 5. 前端设计

### 5.1 页面结构

| 页面 | 路由 | 描述 |
|------|------|------|
| 首页 | `/` | 文章列表、热门文章、分类导航 |
| 文章详情 | `/article/{id}` 或 `/article/{slug}` | 文章内容、评论区 |
| 分类页 | `/category/{id}` | 该分类下的文章 |
| 标签页 | `/tag/{id}` | 该标签下的文章 |
| 搜索页 | `/search?q=xxx` | 搜索结果 |
| 关于页 | `/about` | 个人介绍 |
| 后台管理 | `/admin` | 文章管理、评论审核 |
| 登录页 | `/login` | 管理员登录 |

### 5.2 设计风格
- **配色**: 中性蓝灰色系，温暖亲切
- **排版**: 清晰的层次，易读的字体
- **响应式**: 适配手机、平板、桌面
- **代码高亮**: 使用 Prism.js 或 highlight.js
- **可选**: 暗黑模式切换

### 5.3 核心组件
- `ArticleList` - 文章列表
- `ArticleCard` - 文章卡片
- `ArticleDetail` - 文章详情
- `CommentSection` - 评论区
- `CategoryNav` - 分类导航
- `SearchBar` - 搜索栏
- `AdminLayout` - 后台布局

---

## 6. 核心功能实现思路

### 6.1 文章编辑
- 使用 Markdown 编辑器（如 Toast UI Editor）
- 支持草稿保存
- 支持预览

### 6.2 搜索功能
- 简单方案：MySQL LIKE 查询（标题+摘要）
- 进阶方案：集成 Elasticsearch（后续迭代）

### 6.3 评论系统
- 支持嵌套回复
- 需要审核后显示
- 简单反垃圾机制（可选）

### 6.4 安全认证
- Spring Security + JWT
- 仅管理员可访问 `/admin/*` 接口
- 密码 BCrypt 加密存储

---

## 7. 第一阶段实施范围

### MVP 功能清单
- ✅ 文章 CRUD（创建、读取、更新、删除）
- ✅ 分类和标签管理
- ✅ 评论系统（含审核）
- ✅ 搜索功能
- ✅ 管理员认证
- ✅ 响应式前端

### 后续迭代方向
- 用户注册/登录（访客账户）
- 点赞/收藏功能
- RSS 订阅
- 访客统计
- 图片上传
- SEO 优化

---

## 8. 部署方案

### 开发环境
- 前端：Vite 开发服务器（`npm run dev`）
- 后端：Spring Boot 开发服务器
- 数据库：本地 MySQL

### 生产环境
- 构建前端：`npm run build`，复制到 `backend/src/main/resources/static`
- 构建后端：`mvn clean package`
- 部署：运行 JAR 包，配置生产环境数据库
