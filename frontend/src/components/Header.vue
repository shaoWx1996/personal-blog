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
