<template>
  <div class="bg-white rounded-lg shadow-md p-6">
    <h3 class="text-lg font-bold text-gray-800 mb-4">最新文章</h3>
    <ul class="space-y-3">
      <li v-for="article in recentArticles" :key="article.id">
        <router-link 
          :to="`/article/${article.slug}`"
          class="flex items-center space-x-3 text-gray-600 hover:text-blue-600 transition-colors"
        >
          <div class="w-2 h-2 bg-blue-500 rounded-full mt-2"></div>
          <span class="text-sm truncate">{{ article.title }}</span>
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { articleApi } from '../api'

const recentArticles = ref([])

onMounted(async () => {
  try {
    const response = await articleApi.getArticles({ page: 0, size: 5 })
    recentArticles.value = response.data.content
  } catch (error) {
    console.error('Failed to fetch recent articles:', error)
  }
})
</script>
