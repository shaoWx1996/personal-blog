<template>
  <div class="bg-white rounded-lg shadow-md p-6">
    <h3 class="text-lg font-bold text-gray-800 mb-4">评论 ({{ comments.length }})</h3>
    <div v-if="comments.length === 0" class="text-gray-500 text-center py-8">
      暂无评论，快来发表第一条评论吧！
    </div>
    <ul v-else class="space-y-4">
      <li 
        v-for="comment in comments" 
        :key="comment.id"
        class="border-b border-gray-100 pb-4"
      >
        <div class="flex items-start space-x-3">
          <div class="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0">
            <span class="text-blue-600 font-medium">{{ comment.authorName.charAt(0) }}</span>
          </div>
          <div class="flex-1">
            <div class="flex items-center space-x-2">
              <span class="font-medium text-gray-800">{{ comment.authorName }}</span>
              <span class="text-sm text-gray-400">{{ formatDate(comment.createdAt) }}</span>
            </div>
            <div class="mt-2 text-gray-600">
              <MarkdownPreview :content="comment.content" />
            </div>
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup>
import MarkdownPreview from './MarkdownPreview.vue'

defineProps({
  comments: {
    type: Array,
    default: () => []
  }
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}
</script>
