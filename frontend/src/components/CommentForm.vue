<template>
  <div class="bg-white rounded-lg shadow-md p-6">
    <h3 class="text-lg font-bold text-gray-800 mb-4">发表评论</h3>
    <form @submit.prevent="handleSubmit">
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-2">昵称</label>
        <input
          v-model="form.authorName"
          type="text"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
          placeholder="请输入昵称"
        />
      </div>
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-2">邮箱（选填）</label>
        <input
          v-model="form.authorEmail"
          type="email"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
          placeholder="请输入邮箱"
        />
      </div>
      <div class="mb-4">
        <div class="flex items-center justify-between mb-2">
          <label class="block text-sm font-medium text-gray-700">评论内容</label>
          <button
            type="button"
            @click="isPreview = !isPreview"
            class="text-sm text-blue-600 hover:text-blue-700"
          >
            {{ isPreview ? '编辑' : '预览' }}
          </button>
        </div>
        <textarea
          v-if="!isPreview"
          v-model="form.content"
          rows="4"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500 resize-none"
          placeholder="请输入评论内容（支持 Markdown）"
        ></textarea>
        <div 
          v-else 
          class="w-full px-3 py-2 border border-gray-300 rounded-lg min-h-[100px] bg-gray-50"
        >
          <MarkdownPreview 
            v-if="form.content" 
            :content="form.content" 
          />
          <p v-else class="text-gray-400">暂无内容</p>
        </div>
      </div>
      <button
        type="submit"
        :disabled="submitting"
        class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
      >
        {{ submitting ? '提交中...' : '提交评论' }}
      </button>
      <p v-if="message" class="mt-3 text-sm text-green-600">{{ message }}</p>
    </form>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { commentApi } from '../api'
import MarkdownPreview from './MarkdownPreview.vue'

const props = defineProps({
  articleId: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['commentCreated'])

const form = reactive({
  authorName: '',
  authorEmail: '',
  content: ''
})

const submitting = ref(false)
const message = ref('')
const isPreview = ref(false)

const handleSubmit = async () => {
  if (!form.authorName || !form.content) {
    alert('请填写昵称和评论内容')
    return
  }
  
  submitting.value = true
  
  try {
    await commentApi.createComment(props.articleId, form)
    message.value = '评论提交成功，等待审核'
    form.authorName = ''
    form.authorEmail = ''
    form.content = ''
    isPreview.value = false
    emit('commentCreated')
    setTimeout(() => {
      message.value = ''
    }, 3000)
  } catch (error) {
    console.error('Failed to create comment:', error)
    alert('评论提交失败')
  } finally {
    submitting.value = false
  }
}
</script>
