<template>
  <div class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow">
    <router-link :to="`/article/${article.slug}`">
      <div v-if="article.coverImage" class="h-48 bg-gray-200">
        <img :src="article.coverImage" :alt="article.title" class="w-full h-full object-cover" />
      </div>
      <div class="p-6">
        <div class="flex items-center space-x-2 mb-3">
          <span 
            v-if="article.category"
            class="px-3 py-1 text-xs bg-blue-100 text-blue-600 rounded-full"
          >
            {{ article.category.name }}
          </span>
          <span class="text-sm text-gray-500">{{ formatDate(article.createdAt) }}</span>
        </div>
        <h3 class="text-xl font-bold text-gray-800 mb-2 line-clamp-2">{{ article.title }}</h3>
        <p class="text-gray-600 text-sm mb-4 line-clamp-2">{{ article.summary }}</p>
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-2">
            <span 
              v-for="tag in article.tags?.slice(0, 3)" 
              :key="tag.id"
              class="px-2 py-1 text-xs bg-gray-100 text-gray-600 rounded"
            >
              {{ tag.name }}
            </span>
          </div>
          <div class="flex items-center space-x-4 text-sm text-gray-500">
            <span>{{ article.viewCount }} 阅读</span>
            <span>{{ article.commentCount }} 评论</span>
          </div>
        </div>
      </div>
    </router-link>
  </div>
</template>

<script setup>
defineProps({
  article: {
    type: Object,
    required: true
  }
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
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
