<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <h2 class="text-xl font-bold text-gray-800">{{ isEdit ? '编辑文章' : '新建文章' }}</h2>
      <button
        @click="handleSubmit"
        :disabled="submitting"
        class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
      >
        {{ submitting ? '保存中...' : '保存' }}
      </button>
    </div>
    
    <div class="bg-white rounded-lg shadow-md p-6">
      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">标题</label>
        <input
          v-model="form.title"
          type="text"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
          placeholder="请输入文章标题"
        />
      </div>
      
      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">摘要</label>
        <textarea
          v-model="form.summary"
          rows="3"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500 resize-none"
          placeholder="请输入文章摘要"
        ></textarea>
      </div>
      
      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">内容 (Markdown)</label>
        <textarea
          v-model="form.content"
          rows="20"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500 resize-none font-mono text-sm"
          placeholder="请输入文章内容（支持 Markdown）"
        ></textarea>
      </div>
      
      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">封面图片</label>
        <input
          v-model="form.coverImage"
          type="text"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
          placeholder="请输入封面图片 URL"
        />
      </div>
      
      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">分类</label>
        <select
          v-model="form.categoryId"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
        >
          <option value="">请选择分类</option>
          <option v-for="cat in categories" :key="cat.id" :value="cat.id">
            {{ cat.name }}
          </option>
        </select>
      </div>
      
      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">标签</label>
        <div class="flex flex-wrap gap-2">
          <label
            v-for="tag in tags"
            :key="tag.id"
            class="flex items-center space-x-2 cursor-pointer"
          >
            <input
              type="checkbox"
              :checked="form.tagIds.includes(tag.id)"
              @change="toggleTag(tag.id)"
              class="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
            />
            <span class="text-sm text-gray-700">{{ tag.name }}</span>
          </label>
        </div>
      </div>
      
      <div class="mb-6">
        <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
        <select
          v-model="form.status"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
        >
          <option value="DRAFT">草稿</option>
          <option value="PUBLISHED">发布</option>
        </select>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { articleApi, categoryApi, tagApi } from '../../api'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  title: '',
  summary: '',
  content: '',
  coverImage: '',
  categoryId: null,
  tagIds: [],
  status: 'DRAFT'
})

const categories = ref([])
const tags = ref([])
const submitting = ref(false)

const toggleTag = (tagId) => {
  const index = form.tagIds.indexOf(tagId)
  if (index === -1) {
    form.tagIds.push(tagId)
  } else {
    form.tagIds.splice(index, 1)
  }
}

const loadCategories = async () => {
  try {
    const response = await categoryApi.getCategories()
    categories.value = response.data
  } catch (error) {
    console.error('Failed to fetch categories:', error)
  }
}

const loadTags = async () => {
  try {
    const response = await tagApi.getTags()
    tags.value = response.data
  } catch (error) {
    console.error('Failed to fetch tags:', error)
  }
}

const loadArticle = async () => {
  if (!isEdit.value) return
  
  try {
    const response = await articleApi.getArticleById(route.params.id)
    const article = response.data
    form.title = article.title
    form.summary = article.summary
    form.content = article.content
    form.coverImage = article.coverImage
    form.categoryId = article.category?.id || null
    form.tagIds = article.tags?.map(t => t.id) || []
    form.status = article.status
  } catch (error) {
    console.error('Failed to fetch article:', error)
  }
}

const handleSubmit = async () => {
  if (!form.title || !form.content) {
    alert('请填写标题和内容')
    return
  }
  
  submitting.value = true
  
  try {
    if (isEdit.value) {
      await articleApi.updateArticle(route.params.id, form)
    } else {
      await articleApi.createArticle(form)
    }
    router.push('/admin/articles')
  } catch (error) {
    console.error('Failed to save article:', error)
    alert('保存失败')
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  await loadCategories()
  await loadTags()
  await loadArticle()
})
</script>
