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
