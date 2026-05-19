<template>
  <article
    class="group relative overflow-hidden rounded-2xl transition-all duration-500 hover:scale-[1.02] hover:shadow-2xl glass neon-glow-hover cursor-pointer"
  >
    <router-link :to="`/article/${article.id}`" class="block">
      <!-- 渐变边框效果 -->
      <div class="absolute inset-0 rounded-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-300">
        <div class="absolute inset-0 rounded-2xl p-[2px] bg-gradient-to-r from-neon-blue via-neon-purple to-neon-cyan">
          <div class="w-full h-full rounded-2xl bg-gray-50 dark:bg-dark-card"></div>
        </div>
      </div>

      <div class="relative z-10">
        <!-- 封面图区域 -->
        <div class="relative h-52 overflow-hidden rounded-t-2xl">
          <img
            v-if="article.coverImage"
            :src="article.coverImage"
            :alt="article.title"
            class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110"
          />
          <div v-else class="w-full h-full bg-gradient-to-br from-neon-blue via-neon-purple to-neon-cyan flex items-center justify-center">
            <span class="text-white text-5xl font-bold animate-float">{{ article.title.charAt(0) }}</span>
          </div>

          <!-- 渐变蒙版 -->
          <div class="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent" />

          <!-- 分类标签 -->
          <span
            v-if="article.category"
            class="absolute top-4 left-4 px-3 py-1 text-sm rounded-full glass font-medium backdrop-blur-md"
            :class="isDark ? 'text-neon-blue' : 'text-blue-600'"
          >
            {{ article.category.name }}
          </span>

          <!-- 悬停显示的信息 -->
          <div class="absolute bottom-0 left-0 right-0 p-4 transform translate-y-full group-hover:translate-y-0 transition-transform duration-300">
            <div class="flex items-center justify-between text-white text-sm">
              <span class="flex items-center space-x-1">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                </svg>
                <span>{{ readingTime }} 分钟</span>
              </span>
              <span class="flex items-center space-x-1">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                </svg>
                <span>{{ article.viewCount || 0 }}</span>
              </span>
            </div>
          </div>
        </div>

        <!-- 内容区域 -->
        <div class="p-6">
          <div class="flex items-center justify-between mb-3">
            <span
              class="text-xs"
              :class="isDark ? 'text-gray-400' : 'text-gray-500'"
            >
              {{ formatDate(article.createdAt) }}
            </span>
            <span class="flex items-center space-x-1" :class="isDark ? 'text-gray-400' : 'text-gray-500'">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
              </svg>
              <span>{{ article.likeCount || 0 }}</span>
            </span>
          </div>

          <h3
            class="text-xl font-bold mb-3 line-clamp-2 transition-colors group-hover:neon-text"
            :class="isDark ? 'text-gray-100' : 'text-gray-800'"
          >
            {{ article.title }}
          </h3>

          <p
            class="text-sm line-clamp-2 mb-4"
            :class="isDark ? 'text-gray-400' : 'text-gray-600'"
          >
            {{ article.summary || article.content?.substring(0, 100) + '...' }}
          </p>

          <div class="flex flex-wrap gap-2">
            <span
              v-for="tag in article.tags?.slice(0, 3)"
              :key="tag.id"
              class="text-xs px-2 py-1 rounded-full transition-all duration-300 glass hover:scale-105"
              :class="isDark ? 'text-neon-blue' : 'text-blue-600'"
            >
              #{{ tag.name }}
            </span>
          </div>
        </div>
      </div>
    </router-link>
  </article>
</template>

<script setup>
import { computed } from 'vue'
import { useThemeStore } from '../stores/theme'

const props = defineProps({
  article: {
    type: Object,
    required: true
  }
})

const themeStore = useThemeStore()

const isDark = computed(() => themeStore.isDark)

const readingTime = computed(() => {
  const wordsPerMinute = 200
  const wordCount = props.article.content?.split(/\s+/).length || 0
  return Math.max(1, Math.ceil(wordCount / wordsPerMinute))
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
