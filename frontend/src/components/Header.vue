<template>
  <header 
    class="sticky top-0 z-50 transition-all duration-300"
    :class="isDark ? 'bg-dark-bg/80' : 'bg-white/80'"
    style="backdrop-filter: blur(20px);"
  >
    <div class="max-w-7xl mx-auto px-4">
      <div class="flex items-center justify-between h-20">
        <!-- Logo -->
        <div class="flex items-center space-x-4">
          <h1 class="text-2xl font-bold transition-all duration-300 hover:scale-105">
            <router-link to="/" class="neon-text">
              我的博客
            </router-link>
          </h1>
        </div>
        
        <!-- 导航链接 -->
        <nav class="hidden md:flex items-center space-x-8">
          <router-link 
            to="/" 
            class="transition-all duration-300 hover:neon-text font-medium"
            :class="isDark ? 'text-gray-300' : 'text-gray-600'"
          >
            首页
          </router-link>
          <router-link 
            v-if="isAuthenticated"
            to="/admin" 
            class="transition-all duration-300 hover:neon-text font-medium"
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
              class="w-40 md:w-64 px-4 py-2 text-sm rounded-full transition-all duration-300 focus:outline-none focus:w-72 glass"
              :class="isDark ? 'text-gray-100 placeholder-gray-500' : 'text-gray-900 placeholder-gray-400'"
              @keyup.enter="handleSearch"
              @focus="isSearchFocused = true"
              @blur="isSearchFocused = false"
            />
            <div 
              class="absolute inset-0 rounded-full opacity-0 transition-opacity duration-300 pointer-events-none"
              :class="{ 'opacity-100': isSearchFocused }"
            >
              <div class="absolute inset-0 rounded-full p-[2px] bg-gradient-to-r from-neon-blue to-neon-purple">
                <div class="w-full h-full rounded-full" :class="isDark ? 'bg-dark-card' : 'bg-white'"></div>
              </div>
            </div>
          </div>
          
          <!-- 主题切换按钮 -->
          <button
            @click="toggleTheme"
            class="p-3 rounded-full transition-all duration-300 hover:scale-110 hover:rotate-15 glass neon-glow-hover"
            :title="isDark ? '切换到浅色模式' : '切换到深色模式'"
          >
            <svg v-if="isDark" class="w-6 h-6 text-yellow-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"/>
            </svg>
            <svg v-else class="w-6 h-6 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"/>
            </svg>
          </button>
          
          <!-- 登录/退出 -->
          <button
            v-if="isAuthenticated"
            @click="handleLogout"
            class="px-5 py-2 rounded-full transition-all duration-300 hover:text-red-400 font-medium glass"
            :class="isDark ? 'text-gray-300' : 'text-gray-600'"
          >
            退出
          </button>
          <router-link
            v-else
            to="/login"
            class="px-5 py-2 rounded-full font-medium transition-all duration-300 btn-neon neon-glow-hover transform hover:scale-105"
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
const isSearchFocused = ref(false)

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
