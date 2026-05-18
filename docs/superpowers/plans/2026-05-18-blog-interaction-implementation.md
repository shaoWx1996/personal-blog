# 博客页面交互优化实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 为个人博客实现科技感深色主题 + 社区互动功能的页面交互优化

**架构:** 基于现有 Vue 3 + Tailwind CSS 架构，添加深色/浅色主题系统、微交互动画、社区互动功能

**Tech Stack:** Vue 3, Tailwind CSS, Pinia, GSAP, Canvas API

---

## 文件结构规划

### 新增文件
- `src/stores/theme.js` - 主题状态管理（深色/浅色切换）
- `src/components/HeroSection.vue` - 首页 Hero 区域组件
- `src/components/ParticleBackground.vue` - 粒子背景效果组件
- `src/components/ArticleCardEnhanced.vue` - 增强版文章卡片
- `src/components/ProfileCard.vue` - 个人资料侧边栏卡片
- `src/components/ReadingProgress.vue` - 阅读进度条组件
- `src/components/TableOfContents.vue` - 文章目录导航组件
- `src/components/LikeButton.vue` - 点赞按钮组件（带粒子动画）
- `src/components/ShareButtons.vue` - 分享按钮组件
- `src/composables/useTheme.js` - 主题切换组合式函数
- `src/composables/useReadingTime.js` - 阅读时间计算组合式函数
- `src/utils/animations.js` - 动画工具函数

### 修改文件
- `src/main.js` - 添加主题初始化
- `src/App.vue` - 添加主题切换支持
- `tailwind.config.js` - 添加深色主题配色
- `src/views/Home.vue` - 重构首页布局
- `src/views/ArticleDetail.vue` - 添加互动功能
- `src/components/Header.vue` - 添加主题切换按钮
- `src/components/CommentForm.vue` - 增强评论功能
- `src/components/CommentList.vue` - 优化评论展示

---

## Phase 1: 基础框架（主题系统）

### Task 1: 创建主题状态管理

**Files:**
- Create: `src/stores/theme.js`
- Modify: `src/main.js`

- [ ] **Step 1: 创建 theme store**

```javascript
// src/stores/theme.js
import { defineStore } from 'pinia'
import { ref, watch } from 'vue'

export const useThemeStore = defineStore('theme', () => {
  const isDark = ref(localStorage.getItem('theme') === 'dark' || 
    (!localStorage.getItem('theme') && window.matchMedia('(prefers-color-scheme: dark)').matches))

  const toggleTheme = () => {
    isDark.value = !isDark.value
    localStorage.setItem('theme', isDark.value ? 'dark' : 'light')
    updateDocumentClass()
  }

  const updateDocumentClass = () => {
    if (isDark.value) {
      document.documentElement.classList.add('dark')
    } else {
      document.documentElement.classList.remove('dark')
    }
  }

  const initTheme = () => {
    updateDocumentClass()
  }

  watch(isDark, updateDocumentClass)

  return {
    isDark,
    toggleTheme,
    initTheme
  }
})
```

- [ ] **Step 2: 在 main.js 中初始化主题**

```javascript
// src/main.js
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'
import './style.css'
import { useThemeStore } from './stores/theme'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

// 初始化主题
const themeStore = useThemeStore()
themeStore.initTheme()

app.mount('#app')
```

- [ ] **Step 3: 提交代码**

```bash
git add src/stores/theme.js src/main.js
git commit -m "feat: add theme store with dark/light mode support"
```

---

### Task 2: 配置 Tailwind 深色主题

**Files:**
- Modify: `tailwind.config.js`
- Modify: `src/style.css`

- [ ] **Step 1: 更新 Tailwind 配置**

```javascript
// tailwind.config.js
/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // 深色主题
        dark: {
          bg: '#0f172a',
          card: '#1e293b',
          border: '#334155',
        },
        // 霓虹色
        neon: {
          blue: '#3b82f6',
          purple: '#8b5cf6',
          cyan: '#06b6d4',
        }
      },
      animation: {
        'fade-in': 'fadeIn 0.5s ease-in-out',
        'slide-up': 'slideUp 0.3s ease-out',
        'pulse-glow': 'pulseGlow 2s infinite',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        slideUp: {
          '0%': { transform: 'translateY(10px)', opacity: '0' },
          '100%': { transform: 'translateY(0)', opacity: '1' },
        },
        pulseGlow: {
          '0%, 100%': { boxShadow: '0 0 5px rgba(59, 130, 246, 0.5)' },
          '50%': { boxShadow: '0 0 20px rgba(59, 130, 246, 0.8)' },
        },
      },
    },
  },
  plugins: [],
}
```

- [ ] **Step 2: 更新基础样式**

```css
/* src/style.css */
@tailwind base;
@tailwind components;
@tailwind utilities;

@layer base {
  body {
    @apply transition-colors duration-300;
  }
  
  /* 深色主题默认样式 */
  .dark body {
    @apply bg-dark-bg text-gray-100;
  }
  
  /* 浅色主题样式 */
  body:not(.dark) {
    @apply bg-gray-50 text-gray-900;
  }
}

@layer components {
  /* 卡片样式 */
  .card {
    @apply rounded-lg shadow-md transition-all duration-300;
  }
  
  .dark .card {
    @apply bg-dark-card border border-dark-border;
  }
  
  body:not(.dark) .card {
    @apply bg-white border border-gray-200;
  }
  
  /* 霓虹按钮 */
  .btn-neon {
    @apply relative overflow-hidden transition-all duration-300;
    @apply hover:shadow-lg hover:shadow-neon-blue/50;
  }
  
  .dark .btn-neon {
    @apply bg-gradient-to-r from-neon-blue to-neon-purple text-white;
  }
}
```

- [ ] **Step 3: 提交代码**

```bash
git add tailwind.config.js src/style.css
git commit -m "feat: configure tailwind dark mode and neon color scheme"
```

---

### Task 3: 添加主题切换按钮到导航栏

**Files:**
- Modify: `src/components/Header.vue`

- [ ] **Step 1: 修改 Header 组件**

```vue
<!-- src/components/Header.vue -->
<template>
  <header class="sticky top-0 z-50 transition-colors duration-300"
          :class="isDark ? 'bg-dark-bg/95 backdrop-blur-md border-b border-dark-border' : 'bg-white/95 backdrop-blur-md border-b border-gray-200'">
    <div class="max-w-4xl mx-auto px-4 py-4">
      <div class="flex items-center justify-between">
        <!-- Logo -->
        <div class="flex items-center space-x-4">
          <h1 class="text-xl font-bold transition-colors"
              :class="isDark ? 'text-white' : 'text-gray-800'">
            <router-link to="/" class="hover:text-neon-blue transition-colors">
              我的博客
            </router-link>
          </h1>
        </div>
        
        <!-- 导航链接 -->
        <nav class="hidden md:flex items-center space-x-6">
          <router-link 
            to="/" 
            class="transition-colors hover:text-neon-blue"
            :class="isDark ? 'text-gray-300' : 'text-gray-600'"
          >
            首页
          </router-link>
          <router-link 
            to="/admin" 
            v-if="isAuthenticated"
            class="transition-colors hover:text-neon-blue"
            :class="isDark ? 'text-gray-300' : 'text-gray-600'"
          >
            管理
          </router-link>
        </nav>
        
        <!-- 右侧功能区 -->
        <div class="flex items-center space-x-4">
          <!-- 搜索框 -->
          <div class="relative">
            <input
              type="text"
              v-model="searchQuery"
              placeholder="搜索文章..."
              class="w-40 md:w-64 px-3 py-2 text-sm rounded-full transition-all duration-300 focus:outline-none focus:ring-2 focus:ring-neon-blue"
              :class="isDark ? 'bg-dark-card border-dark-border text-gray-100 placeholder-gray-500' : 'bg-gray-100 border-gray-300 text-gray-900'"
              @keyup.enter="handleSearch"
            />
          </div>
          
          <!-- 主题切换按钮 -->
          <button
            @click="toggleTheme"
            class="p-2 rounded-full transition-all duration-300 hover:scale-110"
            :class="isDark ? 'bg-dark-card text-yellow-400 hover:bg-dark-border' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'"
            :title="isDark ? '切换到浅色模式' : '切换到深色模式'"
          >
            <svg v-if="isDark" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"/>
            </svg>
            <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"/>
            </svg>
          </button>
          
          <!-- 登录/退出 -->
          <button
            v-if="isAuthenticated"
            @click="handleLogout"
            class="px-4 py-2 text-sm transition-colors hover:text-red-400"
            :class="isDark ? 'text-gray-300' : 'text-gray-600'"
          >
            退出
          </button>
          <router-link
            v-else
            to="/login"
            class="px-4 py-2 text-sm rounded-full transition-all duration-300 btn-neon"
          >
            登录
          </router-link>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useAuthStore } from '../store/auth'
import { useThemeStore } from '../stores/theme'

const authStore = useAuthStore()
const themeStore = useThemeStore()
const searchQuery = ref('')

const isAuthenticated = computed(() => authStore.isAuthenticated)
const isDark = computed(() => themeStore.isDark)

const toggleTheme = () => {
  themeStore.toggleTheme()
}

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    window.location.href = `/search?q=${encodeURIComponent(searchQuery.value)}`
  }
}

const handleLogout = () => {
  authStore.logout()
  window.location.href = '/'
}
</script>
```

- [ ] **Step 2: 提交代码**

```bash
git add src/components/Header.vue
git commit -m "feat: add theme toggle button to header"
```

---

## Phase 2: 核心交互组件

### Task 4: 创建增强版文章卡片组件

**Files:**
- Create: `src/components/ArticleCardEnhanced.vue`

- [ ] **Step 1: 创建组件**

```vue
<!-- src/components/ArticleCardEnhanced.vue -->
<template>
  <article 
    class="group card overflow-hidden cursor-pointer transform transition-all duration-300 hover:scale-[1.02] hover:shadow-xl"
    @click="navigateToArticle"
  >
    <!-- 封面图 -->
    <div class="relative h-48 overflow-hidden">
      <img 
        v-if="article.coverImage" 
        :src="article.coverImage" 
        :alt="article.title"
        class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
      />
      <div v-else class="w-full h-full bg-gradient-to-br from-neon-blue to-neon-purple flex items-center justify-center">
        <span class="text-white text-4xl font-bold">{{ article.title.charAt(0) }}</span>
      </div>
      
      <!-- 渐变遮罩 -->
      <div class="absolute inset-0 bg-gradient-to-t from-black/70 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"/>
      
      <!-- 悬停信息 -->
      <div class="absolute bottom-0 left-0 right-0 p-4 transform translate-y-full group-hover:translate-y-0 transition-transform duration-300">
        <div class="flex items-center space-x-4 text-white text-sm">
          <span class="flex items-center space-x-1">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
            <span>{{ readingTime }} 分钟阅读</span>
          </span>
          <span class="flex items-center space-x-1">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
            </svg>
            <span>{{ article.viewCount || 0 }}</span>
          </span>
          <span class="flex items-center space-x-1">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
            </svg>
            <span>{{ article.likeCount || 0 }}</span>
          </span>
        </div>
      </div>
    </div>
    
    <!-- 内容区 -->
    <div class="p-5">
      <!-- 分类标签 -->
      <div class="flex items-center space-x-2 mb-3">
        <span 
          v-if="article.category"
          class="px-2 py-1 text-xs rounded-full transition-colors"
          :class="isDark ? 'bg-neon-blue/20 text-neon-blue' : 'bg-blue-100 text-blue-600'"
        >
          {{ article.category.name }}
        </span>
        <span 
          class="text-xs"
          :class="isDark ? 'text-gray-400' : 'text-gray-500'"
        >
          {{ formatDate(article.createdAt) }}
        </span>
      </div>
      
      <!-- 标题 -->
      <h3 
        class="text-lg font-bold mb-2 line-clamp-2 transition-colors group-hover:text-neon-blue"
        :class="isDark ? 'text-gray-100' : 'text-gray-800'"
      >
        {{ article.title }}
      </h3>
      
      <!-- 摘要 -->
      <p 
        class="text-sm line-clamp-2 mb-4"
        :class="isDark ? 'text-gray-400' : 'text-gray-600'"
      >
        {{ article.summary || article.content.substring(0, 100) + '...' }}
      </p>
      
      <!-- 标签 -->
      <div class="flex flex-wrap gap-2">
        <span 
          v-for="tag in article.tags?.slice(0, 3)" 
          :key="tag.id"
          class="text-xs px-2 py-1 rounded transition-colors"
          :class="isDark ? 'bg-dark-border text-gray-400' : 'bg-gray-100 text-gray-600'"
        >
          #{{ tag.name }}
        </span>
      </div>
    </div>
  </article>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useThemeStore } from '../stores/theme'

const props = defineProps({
  article: {
    type: Object,
    required: true
  }
})

const router = useRouter()
const themeStore = useThemeStore()

const isDark = computed(() => themeStore.isDark)

const readingTime = computed(() => {
  const wordsPerMinute = 200
  const wordCount = props.article.content?.split(/\s+/).length || 0
  return Math.ceil(wordCount / wordsPerMinute)
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    month: 'short',
    day: 'numeric'
  })
}

const navigateToArticle = () => {
  router.push(`/article/${props.article.slug}`)
}
</script>
```

- [ ] **Step 2: 提交代码**

```bash
git add src/components/ArticleCardEnhanced.vue
git commit -m "feat: create enhanced article card with hover effects"
```

---

### Task 5: 创建个人资料卡片组件

**Files:**
- Create: `src/components/ProfileCard.vue`

- [ ] **Step 1: 创建组件**

```vue
<!-- src/components/ProfileCard.vue -->
<template>
  <div class="card p-6 sticky top-24">
    <!-- 头像和基本信息 -->
    <div class="text-center mb-6">
      <div class="relative inline-block">
        <div class="w-20 h-20 rounded-full bg-gradient-to-br from-neon-blue to-neon-purple flex items-center justify-center text-white text-2xl font-bold mb-3">
          {{ avatarText }}
        </div>
        <div class="absolute bottom-0 right-0 w-5 h-5 bg-green-500 rounded-full border-2 border-white"></div>
      </div>
      <h3 class="text-lg font-bold mb-1" :class="isDark ? 'text-white' : 'text-gray-800'">
        博主名称
      </h3>
      <p class="text-sm" :class="isDark ? 'text-gray-400' : 'text-gray-600'">
        全栈开发者 | 技术博主
      </p>
    </div>
    
    <!-- 数据统计 -->
    <div class="grid grid-cols-3 gap-4 mb-6 text-center">
      <div>
        <div class="text-xl font-bold text-neon-blue">{{ stats.articles }}</div>
        <div class="text-xs" :class="isDark ? 'text-gray-400' : 'text-gray-500'">文章</div>
      </div>
      <div>
        <div class="text-xl font-bold text-neon-purple">{{ stats.views }}</div>
        <div class="text-xs" :class="isDark ? 'text-gray-400' : 'text-gray-500'">阅读</div>
      </div>
      <div>
        <div class="text-xl font-bold text-neon-cyan">{{ stats.likes }}</div>
        <div class="text-xs" :class="isDark ? 'text-gray-400' : 'text-gray-500'">点赞</div>
      </div>
    </div>
    
    <!-- 技能标签 -->
    <div class="mb-6">
      <h4 class="text-sm font-semibold mb-3" :class="isDark ? 'text-gray-300' : 'text-gray-700'">
        技术栈
      </h4>
      <div class="flex flex-wrap gap-2">
        <span 
          v-for="skill in skills" 
          :key="skill"
          class="text-xs px-2 py-1 rounded-full transition-all duration-300 hover:scale-105"
          :class="isDark ? 'bg-neon-blue/20 text-neon-blue border border-neon-blue/30' : 'bg-blue-50 text-blue-600 border border-blue-200'"
        >
          {{ skill }}
        </span>
      </div>
    </div>
    
    <!-- 订阅按钮 -->
    <button 
      class="w-full py-2 rounded-lg font-medium transition-all duration-300 btn-neon"
      @click="handleSubscribe"
    >
      <span class="flex items-center justify-center space-x-2">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
        </svg>
        <span>订阅更新</span>
      </span>
    </button>
    
    <!-- 社交链接 -->
    <div class="flex justify-center space-x-4 mt-4">
      <a 
        v-for="link in socialLinks" 
        :key="link.name"
        :href="link.url"
        target="_blank"
        class="p-2 rounded-full transition-all duration-300 hover:scale-110"
        :class="isDark ? 'bg-dark-border text-gray-400 hover:text-neon-blue hover:bg-neon-blue/20' : 'bg-gray-100 text-gray-600 hover:text-blue-600 hover:bg-blue-50'"
      >
        <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
          <path :d="link.icon"/>
        </svg>
      </a>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useThemeStore } from '../stores/theme'

const themeStore = useThemeStore()

const isDark = computed(() => themeStore.isDark)
const avatarText = computed(() => '博')

const stats = ref({
  articles: 25,
  views: '10.5k',
  likes: 856
})

const skills = ['Java', 'Spring Boot', 'Vue 3', 'TypeScript', 'Docker', 'MySQL']

const socialLinks = [
  {
    name: 'GitHub',
    url: 'https://github.com',
    icon: 'M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z'
  },
  {
    name: 'Twitter',
    url: 'https://twitter.com',
    icon: 'M23.953 4.57a10 10 0 01-2.825.775 4.958 4.958 0 002.163-2.723c-.951.555-2.005.959-3.127 1.184a4.92 4.92 0 00-8.384 4.482C7.69 8.095 4.067 6.13 1.64 3.162a4.822 4.822 0 00-.666 2.475c0 1.71.87 3.213 2.188 4.096a4.904 4.904 0 01-2.228-.616v.06a4.923 4.923 0 003.946 4.827 4.996 4.996 0 01-2.212.085 4.936 4.936 0 004.604 3.417 9.867 9.867 0 01-6.102 2.105c-.39 0-.779-.023-1.17-.067a13.995 13.995 0 007.557 2.209c9.053 0 13.998-7.496 13.998-13.985 0-.21 0-.42-.015-.63A9.935 9.935 0 0024 4.59z'
  }
]

const handleSubscribe = () => {
  alert('订阅功能开发中，敬请期待！')
}
</script>
```

- [ ] **Step 2: 提交代码**

```bash
git add src/components/ProfileCard.vue
git commit -m "feat: create profile card component with stats and skills"
```

---

## Phase 3: 文章页互动功能

### Task 6: 创建阅读进度条组件

**Files:**
- Create: `src/components/ReadingProgress.vue`

- [ ] **Step 1: 创建组件**

```vue
<!-- src/components/ReadingProgress.vue -->
<template>
  <div class="fixed top-0 left-0 right-0 h-1 z-[60] bg-transparent">
    <div 
      class="h-full bg-gradient-to-r from-neon-blue via-neon-purple to-neon-cyan transition-all duration-150"
      :style="{ width: progress + '%' }"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const progress = ref(0)

const updateProgress = () => {
  const scrollTop = window.scrollY
  const docHeight = document.documentElement.scrollHeight - window.innerHeight
  progress.value = docHeight > 0 ? (scrollTop / docHeight) * 100 : 0
}

onMounted(() => {
  window.addEventListener('scroll', updateProgress, { passive: true })
  updateProgress()
})

onUnmounted(() => {
  window.removeEventListener('scroll', updateProgress)
})
</script>
```

- [ ] **Step 2: 提交代码**

```bash
git add src/components/ReadingProgress.vue
git commit -m "feat: add reading progress bar component"
```

---

### Task 7: 创建点赞按钮组件（带粒子动画）

**Files:**
- Create: `src/components/LikeButton.vue`

- [ ] **Step 1: 创建组件**

```vue
<!-- src/components/LikeButton.vue -->
<template>
  <button 
    class="relative flex items-center space-x-2 px-4 py-2 rounded-full transition-all duration-300"
    :class="[
      isLiked 
        ? 'bg-red-500/20 text-red-500 border border-red-500/30' 
        : isDark 
          ? 'bg-dark-border text-gray-400 hover:text-red-400' 
          : 'bg-gray-100 text-gray-600 hover:text-red-500'
    ]"
    @click="handleLike"
  >
    <div class="relative">
      <svg 
        class="w-5 h-5 transition-transform duration-300"
        :class="{ 'scale-125': isAnimating }"
        :fill="isLiked ? 'currentColor' : 'none'"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path 
          stroke-linecap="round" 
          stroke-linejoin="round" 
          stroke-width="2" 
          d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
        />
      </svg>
      
      <!-- 粒子效果 -->
      <canvas 
        v-if="showParticles"
        ref="particleCanvas"
        class="absolute inset-0 pointer-events-none"
        width="40"
        height="40"
        style="transform: translate(-50%, -50%); left: 50%; top: 50%;"
      />
    </div>
    
    <span class="font-medium">{{ formattedCount }}</span>
  </button>
</template>

<script setup>
import { ref, computed, nextTick } from 'vue'
import { useThemeStore } from '../stores/theme'

const props = defineProps({
  count: {
    type: Number,
    default: 0
  },
  articleId: {
    type: [Number, String],
    required: true
  }
})

const themeStore = useThemeStore()
const isDark = computed(() => themeStore.isDark)

const isLiked = ref(false)
const isAnimating = ref(false)
const showParticles = ref(false)
const particleCanvas = ref(null)
const currentCount = ref(props.count)

const formattedCount = computed(() => {
  if (currentCount.value >= 1000) {
    return (currentCount.value / 1000).toFixed(1) + 'k'
  }
  return currentCount.value
})

const handleLike = async () => {
  if (isAnimating.value) return
  
  isAnimating.value = true
  
  if (!isLiked.value) {
    // 点赞
    isLiked.value = true
    currentCount.value++
    showParticles.value = true
    
    await nextTick()
    createParticleExplosion()
    
    setTimeout(() => {
      showParticles.value = false
    }, 1000)
  } else {
    // 取消点赞
    isLiked.value = false
    currentCount.value--
  }
  
  // 这里可以调用 API 保存点赞状态
  // await articleApi.likeArticle(props.articleId)
  
  setTimeout(() => {
    isAnimating.value = false
  }, 300)
}

const createParticleExplosion = () => {
  const canvas = particleCanvas.value
  if (!canvas) return
  
  const ctx = canvas.getContext('2d')
  const particles = []
  const particleCount = 12
  
  for (let i = 0; i < particleCount; i++) {
    particles.push({
      x: 20,
      y: 20,
      vx: (Math.random() - 0.5) * 8,
      vy: (Math.random() - 0.5) * 8,
      life: 1,
      color: `hsl(${Math.random() * 60 + 330}, 100%, 60%)`
    })
  }
  
  const animate = () => {
    ctx.clearRect(0, 0, 40, 40)
    
    let hasLivingParticles = false
    
    particles.forEach(p => {
      if (p.life > 0) {
        hasLivingParticles = true
        p.x += p.vx
        p.y += p.vy
        p.life -= 0.02
        
        ctx.beginPath()
        ctx.arc(p.x, p.y, 2 * p.life, 0, Math.PI * 2)
        ctx.fillStyle = p.color
        ctx.globalAlpha = p.life
        ctx.fill()
      }
    })
    
    if (hasLivingParticles) {
      requestAnimationFrame(animate)
    }
  }
  
  animate()
}
</script>
```

- [ ] **Step 2: 提交代码**

```bash
git add src/components/LikeButton.vue
git commit -m "feat: create like button with particle explosion animation"
```

---

## 后续任务规划

### Phase 4: 社区功能
- Task 8: 分享按钮组件
- Task 9: 评论系统增强（Markdown 支持）
- Task 10: 邮件订阅功能

### Phase 5: 高级功能
- Task 11: 粒子背景效果
- Task 12: 时间轴展示页面
- Task 13: 项目展示页面

---

## 测试检查清单

- [ ] 主题切换在所有页面正常工作
- [ ] 深色/浅色模式颜色对比度符合 WCAG 标准
- [ ] 文章卡片悬停效果流畅
- [ ] 点赞动画在所有浏览器正常显示
- [ ] 移动端响应式布局正确
- [ ] 阅读进度条滚动同步准确
