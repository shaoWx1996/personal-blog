<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <h2 class="text-xl font-bold text-gray-800">评论管理</h2>
    </div>
    
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">文章标题</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">作者</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">内容</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">创建时间</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr v-for="comment in comments" :key="comment.id">
            <td class="px-6 py-4 text-blue-600 hover:underline max-w-xs truncate">
              {{ comment.articleTitle }}
            </td>
            <td class="px-6 py-4 text-gray-900">{{ comment.authorName }}</td>
            <td class="px-6 py-4 text-gray-600 max-w-xs truncate">{{ comment.content }}</td>
            <td class="px-6 py-4">
              <span 
                class="px-2 py-1 text-xs rounded-full"
                :class="comment.isApproved ? 'bg-green-100 text-green-600' : 'bg-yellow-100 text-yellow-600'"
              >
                {{ comment.isApproved ? '已审核' : '待审核' }}
              </span>
            </td>
            <td class="px-6 py-4 text-gray-600">
              {{ formatDate(comment.createdAt) }}
            </td>
            <td class="px-6 py-4">
              <div class="flex items-center space-x-2">
                <button
                  v-if="!comment.isApproved"
                  @click="handleApprove(comment.id)"
                  class="px-3 py-1 text-sm text-green-600 hover:text-green-800"
                >
                  审核通过
                </button>
                <button
                  @click="handleDelete(comment.id)"
                  class="px-3 py-1 text-sm text-red-600 hover:text-red-800"
                >
                  删除
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-if="comments.length === 0" class="text-center py-12 text-gray-500">
        暂无评论
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { commentApi, articleApi } from '../../api'

const comments = ref([])

const fetchComments = async () => {
  try {
    const response = await commentApi.getAllComments()
    const commentsWithArticle = await Promise.all(
      response.data.map(async comment => {
        const article = await articleApi.getArticleById(comment.articleId)
        return {
          ...comment,
          articleTitle: article.data.title
        }
      })
    )
    comments.value = commentsWithArticle
  } catch (error) {
    console.error('Failed to fetch comments:', error)
  }
}

const handleApprove = async (id) => {
  try {
    await commentApi.approveComment(id)
    const comment = comments.value.find(c => c.id === id)
    if (comment) {
      comment.isApproved = true
    }
  } catch (error) {
    console.error('Failed to approve comment:', error)
  }
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除这条评论吗？')) return
  
  try {
    await commentApi.deleteComment(id)
    comments.value = comments.value.filter(c => c.id !== id)
  } catch (error) {
    console.error('Failed to delete comment:', error)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

onMounted(() => {
  fetchComments()
})
</script>
