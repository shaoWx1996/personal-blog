<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <h2 class="text-xl font-bold text-gray-800">文章列表</h2>
      <router-link 
        to="/admin/articles/new" 
        class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
      >
        新建文章
      </router-link>
    </div>
    
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">标题</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">分类</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">创建时间</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr v-for="article in articles" :key="article.id">
            <td class="px-6 py-4">
              <a :href="`/article/${article.slug}`" target="_blank" class="text-blue-600 hover:underline">
                {{ article.title }}
              </a>
            </td>
            <td class="px-6 py-4 text-gray-600">
              {{ article.category?.name || '-' }}
            </td>
            <td class="px-6 py-4">
              <span 
                class="px-2 py-1 text-xs rounded-full"
                :class="article.status === 'PUBLISHED' ? 'bg-green-100 text-green-600' : 'bg-gray-100 text-gray-600'"
              >
                {{ article.status === 'PUBLISHED' ? '已发布' : '草稿' }}
              </span>
            </td>
            <td class="px-6 py-4 text-gray-600">
              {{ formatDate(article.createdAt) }}
            </td>
            <td class="px-6 py-4">
              <div class="flex items-center space-x-2">
                <router-link 
                  :to="`/admin/articles/${article.id}/edit`" 
                  class="px-3 py-1 text-sm text-blue-600 hover:text-blue-800"
                >
                  编辑
                </router-link>
                <button
                  @click="handleDelete(article.id)"
                  class="px-3 py-1 text-sm text-red-600 hover:text-red-800"
                >
                  删除
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-if="articles.length === 0" class="text-center py-12 text-gray-500">
        暂无文章
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { articleApi } from '../../api'

const articles = ref([])

const fetchArticles = async () => {
  try {
    const response = await articleApi.getArticles({ page: 0, size: 100 })
    articles.value = response.data.content
  } catch (error) {
    console.error('Failed to fetch articles:', error)
  }
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除这篇文章吗？')) return
  
  try {
    await articleApi.deleteArticle(id)
    articles.value = articles.value.filter(a => a.id !== id)
  } catch (error) {
    console.error('Failed to delete article:', error)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

onMounted(() => {
  fetchArticles()
})
</script>
