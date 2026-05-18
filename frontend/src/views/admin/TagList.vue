<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <h2 class="text-xl font-bold text-gray-800">标签管理</h2>
      <button
        @click="showModal = true"
        class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
      >
        新建标签
      </button>
    </div>
    
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">名称</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Slug</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr v-for="tag in tags" :key="tag.id">
            <td class="px-6 py-4 font-medium text-gray-900">{{ tag.name }}</td>
            <td class="px-6 py-4 text-gray-600">{{ tag.slug }}</td>
            <td class="px-6 py-4">
              <div class="flex items-center space-x-2">
                <button
                  @click="handleEdit(tag)"
                  class="px-3 py-1 text-sm text-blue-600 hover:text-blue-800"
                >
                  编辑
                </button>
                <button
                  @click="handleDelete(tag.id)"
                  class="px-3 py-1 text-sm text-red-600 hover:text-red-800"
                >
                  删除
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-if="tags.length === 0" class="text-center py-12 text-gray-500">
        暂无标签
      </div>
    </div>
    
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <h3 class="text-lg font-bold text-gray-800 mb-4">{{ editingTag ? '编辑标签' : '新建标签' }}</h3>
        
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2">名称</label>
          <input
            v-model="modalForm.name"
            type="text"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
            placeholder="请输入标签名称"
          />
        </div>
        
        <div class="mb-6">
          <label class="block text-sm font-medium text-gray-700 mb-2">Slug</label>
          <input
            v-model="modalForm.slug"
            type="text"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
            placeholder="请输入 slug（可选）"
          />
        </div>
        
        <div class="flex justify-end space-x-3">
          <button
            @click="showModal = false"
            class="px-4 py-2 text-gray-600 hover:text-gray-800"
          >
            取消
          </button>
          <button
            @click="handleSave"
            class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            保存
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { tagApi } from '../../api'

const tags = ref([])
const showModal = ref(false)
const editingTag = ref(null)
const modalForm = reactive({
  name: '',
  slug: ''
})

const fetchTags = async () => {
  try {
    const response = await tagApi.getTags()
    tags.value = response.data
  } catch (error) {
    console.error('Failed to fetch tags:', error)
  }
}

const handleEdit = (tag) => {
  editingTag.value = tag
  modalForm.name = tag.name
  modalForm.slug = tag.slug
  showModal.value = true
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除这个标签吗？')) return
  
  try {
    await tagApi.deleteTag(id)
    tags.value = tags.value.filter(t => t.id !== id)
  } catch (error) {
    console.error('Failed to delete tag:', error)
  }
}

const handleSave = async () => {
  if (!modalForm.name) {
    alert('请输入标签名称')
    return
  }
  
  try {
    if (editingTag.value) {
      await tagApi.updateTag(editingTag.value.id, modalForm)
    } else {
      await tagApi.createTag(modalForm)
    }
    showModal.value = false
    editingTag.value = null
    modalForm.name = ''
    modalForm.slug = ''
    await fetchTags()
  } catch (error) {
    console.error('Failed to save tag:', error)
    alert('保存失败')
  }
}

onMounted(() => {
  fetchTags()
})
</script>
