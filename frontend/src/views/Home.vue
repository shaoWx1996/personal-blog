<template>
  <div class="min-h-screen">
    <!-- 全屏 Hero 区域 -->
    <section class="relative h-screen flex items-center justify-center overflow-hidden">
      <!-- 粒子背景 -->
      <HeroParticles />

      <!-- 渐变蒙版 -->
      <div class="absolute inset-0 bg-gradient-to-b from-transparent via-transparent to-gray-50 dark:to-dark-bg" />

      <!-- Hero 内容 -->
      <div class="relative z-10 text-center px-4 animate-fade-in-up">
        <h1 class="text-5xl md:text-7xl font-bold mb-6 neon-text animate-glow-pulse">
          欢迎来到我的博客
        </h1>
        <p class="text-xl md:text-2xl text-gray-600 dark:text-gray-300 mb-8 max-w-2xl mx-auto">
          技术与生活的点滴分享
        </p>
        <div class="flex justify-center gap-4">
          <router-link
            to="/"
            class="px-8 py-3 rounded-full font-medium transition-all duration-300 btn-neon neon-glow-hover transform hover:scale-105"
          >
            浏览文章
          </router-link>
          <router-link
            v-if="isAuthenticated"
            to="/admin"
            class="px-8 py-3 rounded-full font-medium transition-all duration-300 glass border border-white/30 hover:border-neon-blue/50 transform hover:scale-105"
            :class="isDark ? 'text-white' : 'text-gray-800'"
          >
            管理后台
          </router-link>
        </div>
      </div>

      <!-- 滚动指示器 -->
      <div class="absolute bottom-10 left-1/2 -translate-x-1/2 animate-bounce-slow">
        <div class="flex flex-col items-center text-gray-500 dark:text-gray-400">
          <span class="text-sm mb-2">向下滚动</span>
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m7 7V3"/>
          </svg>
        </div>
      </div>
    </section>

    <!-- 文章列表区域 -->
    <section class="max-w-7xl mx-auto px-4 py-16">
      <div class="text-center mb-12 animate-fade-in-up">
        <h2 class="text-3xl md:text-4xl font-bold mb-4 neon-text">最新文章</h2>
        <p class="text-gray-600 dark:text-gray-400">探索技术世界，分享生活感悟</p>
      </div>

      <!-- 瀑布流文章卡片 -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <ArticleCardEnhanced
          v-for="(article, index) in articles"
          :key="article.id"
          :article="article"
          :style="{ animationDelay: `${index * 0.1}s` }"
          class="animate-fade-in-up opacity-0"
          :class="{ 'opacity-100': true }"
        />
      </div>

      <!-- 分页 -->
      <div v-if="totalPages > 1" class="mt-12 flex justify-center">
        <nav class="flex items-center space-x-2">
          <button
            @click="goToPage(currentPage - 1)"
            :disabled="currentPage === 0"
            class="px-4 py-2 rounded-lg transition-all duration-300 glass disabled:opacity-50 disabled:cursor-not-allowed hover:neon-glow"
            :class="isDark ? 'text-white' : 'text-gray-800'"
          >
            上一页
          </button>
          <span class="px-4 py-2" :class="isDark ? 'text-gray-300' : 'text-gray-600'">
            第 {{ currentPage + 1 }} / {{ totalPages }} 页
          </span>
          <button
            @click="goToPage(currentPage + 1)"
            :disabled="currentPage >= totalPages - 1"
            class="px-4 py-2 rounded-lg transition-all duration-300 glass disabled:opacity-50 disabled:cursor-not-allowed hover:neon-glow"
            :class="isDark ? 'text-white' : 'text-gray-800'"
          >
            下一页
          </button>
        </nav>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useAuthStore } from '../store/auth'
import { useThemeStore } from '../stores/theme'
import HeroParticles from '../components/HeroParticles.vue'
import ArticleCardEnhanced from '../components/ArticleCardEnhanced.vue'
import { articleApi } from '../api'

const authStore = useAuthStore()
const themeStore = useThemeStore()

const isAuthenticated = computed(() => authStore.isAuthenticated)
const isDark = computed(() => themeStore.isDark)

const articles = ref([])
const currentPage = ref(0)
const totalPages = ref(0)

const fetchArticles = async (page = 0) => {
  try {
    const response = await articleApi.getArticles({ page, size: 9 })
    articles.value = response.data.content
    totalPages.value = response.data.totalPages
    currentPage.value = page
  } catch (error) {
    console.error('Failed to fetch articles:', error)
  }
}

const goToPage = (page) => {
  if (page >= 0 && page < totalPages.value) {
    fetchArticles(page)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

onMounted(() => {
  fetchArticles()
})
</script>

<style scoped>
.animate-fade-in-up {
  animation: fadeInUp 0.6s ease-out forwards;
}
</style>
