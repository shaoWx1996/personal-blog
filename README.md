# Personal Blog

一个现代化的个人博客系统，支持技术文章与生活随笔，集成社区互动与个人品牌展示功能。

## 技术栈

### 后端
- **Spring Boot 3.2.0** - 核心框架
- **Spring Security** - 安全认证
- **Spring Data JPA** - 数据访问
- **MySQL** - 数据库
- **JWT** - 身份认证

### 前端
- **Vue 3** - 渐进式JavaScript框架
- **Vite** - 下一代前端构建工具
- **Pinia** - 状态管理
- **Vue Router** - 路由管理
- **Tailwind CSS** - 样式框架
- **Marked + Highlight.js** - Markdown渲染与代码高亮

## 功能特性

### 核心功能
- 📝 **文章管理** - 创建、编辑、发布、删除文章
- 🏷️ **分类标签** - 文章分类与标签系统
- 💬 **评论系统** - 支持Markdown的评论功能
- 🔍 **搜索功能** - 快速检索文章

### 交互优化
- 🌙 **深色/浅色主题** - 一键切换的视觉体验
- ✨ **阅读进度条** - 实时显示阅读进度
- ❤️ **点赞动画** - 粒子爆炸效果的点赞交互
- 🔗 **分享功能** - 多平台文章分享
- 📊 **个人品牌** - 展示个人资料与技能标签

### 增强组件
- 增强版文章卡片（阅读时间、阅读量、点赞数）
- 个人资料卡片（统计数据、技能标签、社交链接）
- Markdown代码高亮预览

## 项目结构

```
personal-blog/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/blog/
│   │   ├── config/           # 配置类
│   │   ├── controller/       # 控制器
│   │   ├── dto/              # 数据传输对象
│   │   ├── entity/           # 实体类
│   │   ├── repository/       # 数据仓库
│   │   ├── security/         # 安全认证
│   │   └── service/          # 业务服务
│   └── src/main/resources/
│       └── application.yml   # 应用配置
│
└── frontend/                  # Vue 3 前端
    ├── src/
    │   ├── api/              # API接口
    │   ├── components/       # 组件
    │   ├── router/          # 路由配置
    │   ├── store/           # 状态管理
    │   ├── stores/          # Pinia状态
    │   ├── views/           # 页面视图
    │   └── App.vue          # 根组件
    └── package.json
```

## 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.6+

### 后端启动

1. **配置数据库**
   ```bash
   # 创建数据库
   mysql -u root -p
   CREATE DATABASE personal_blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **修改配置文件**
   编辑 `backend/src/main/resources/application.yml`：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/personal_blog?useSSL=false&serverTimezone=Asia/Shanghai
       username: your_username
       password: your_password
   ```

3. **启动后端服务**
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   服务地址：http://localhost:8080

### 前端启动

1. **安装依赖**
   ```bash
   cd frontend
   npm install
   ```

2. **启动开发服务器**
   ```bash
   npm run dev
   ```
   访问地址：http://localhost:5173

### 默认账号
- 用户名：`admin`
- 密码：`admin123`

## API文档

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册

### 文章接口
- `GET /api/articles` - 获取文章列表
- `GET /api/articles/{id}` - 获取文章详情
- `POST /api/articles` - 创建文章（需认证）
- `PUT /api/articles/{id}` - 更新文章（需认证）
- `DELETE /api/articles/{id}` - 删除文章（需认证）

### 分类接口
- `GET /api/categories` - 获取分类列表
- `POST /api/categories` - 创建分类（需认证）
- `PUT /api/categories/{id}` - 更新分类（需认证）
- `DELETE /api/categories/{id}` - 删除分类（需认证）

### 标签接口
- `GET /api/tags` - 获取标签列表
- `POST /api/tags` - 创建标签（需认证）
- `PUT /api/tags/{id}` - 更新标签（需认证）
- `DELETE /api/tags/{id}` - 删除标签（需认证）

### 评论接口
- `GET /api/articles/{id}/comments` - 获取文章评论
- `POST /api/articles/{id}/comments` - 添加评论
- `DELETE /api/comments/{id}` - 删除评论（需认证）

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 贡献

欢迎提交 Issue 和 Pull Request！
