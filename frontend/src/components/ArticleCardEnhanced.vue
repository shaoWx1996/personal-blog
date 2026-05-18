<template>
  <article 
    class="group card overflow-hidden cursor-pointer transform transition-all duration-300 hover:scale-[1.02] hover:shadow-xl"
  >
    <router-link :to="`/article/${article.slug}`">
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
        
        <div class="absolute inset-0 bg-gradient-to-t from-black/70 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"/>
        
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
      
      <div class="p-5">
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
        
        <h3 
          class="text-lg font-bold mb-2 line-clamp-2 transition-colors group-hover:text-neon-blue"
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
            class="text-xs px-2 py-1 rounded transition-colors"
            :class="isDark ? 'bg-dark-border text-gray-400' : 'bg-gray-100 text-gray-600'"
          >
            #{{ tag.name }}
          </span>
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
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
