<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <h2 class="text-xl font-bold text-gray-800">分类管理</h2>
      <button
        @click="showModal = true"
        class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
      >
        新建分类
      </button>
    </div>
    
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">名称</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Slug</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">描述</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr v-for="category in categories" :key="category.id">
            <td class="px-6 py-4 font-medium text-gray-900">{{ category.name }}</td>
            <td class="px-6 py-4 text-gray-600">{{ category.slug }}</td>
            <td class="px-6 py-4 text-gray-600 max-w-xs truncate">{{ category.description }}</td>
            <td class="px-6 py-4">
              <div class="flex items-center space-x-2">
                <button
                  @click="handleEdit(category)"
                  class="px-3 py-1 text-sm text-blue-600 hover:text-blue-800"
                >
                  编辑
                </button>
                <button
                  @click="handleDelete(category.id)"
                  class="px-3 py-1 text-sm text-red-600 hover:text-red-800"
                >
                  删除
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-if="categories.length === 0" class="text-center py-12 text-gray-500">
        暂无分类
      </div>
    </div>
    
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <h3 class="text-lg font-bold text-gray-800 mb-4">{{ editingCategory ? '编辑分类' : '新建分类' }}</h3>
        
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2">名称</label>
          <input
            v-model="modalForm.name"
            type="text"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
            placeholder="请输入分类名称"
          />
        </div>
        
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2">Slug</label>
          <input
            v-model="modalForm.slug"
            type="text"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
            placeholder="请输入 slug（可选）"
          />
        </div>
        
        <div class="mb-6">
          <label class="block text-sm font-medium text-gray-700 mb-2">描述</label>
          <textarea
            v-model="modalForm.description"
            rows="3"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500 resize-none"
            placeholder="请输入描述（可选）"
          ></textarea>
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
import { categoryApi } from '../../api'

const categories = ref([])
const showModal = ref(false)
const editingCategory = ref(null)
const modalForm = reactive({
  name: '',
  slug: '',
  description: ''
})

const fetchCategories = async () => {
  try {
    const response = await categoryApi.getCategories()
    categories.value = response.data
  } catch (error) {
    console.error('Failed to fetch categories:', error)
  }
}

const handleEdit = (category) => {
  editingCategory.value = category
  modalForm.name = category.name
  modalForm.slug = category.slug
  modalForm.description = category.description || ''
  showModal.value = true
}

const handleDelete = async (id) => {
  if (!confirm('确定要删除这个分类吗？')) return
  
  try {
    await categoryApi.deleteCategory(id)
    categories.value = categories.value.filter(c => c.id !== id)
  } catch (error) {
    console.error('Failed to delete category:', error)
  }
}

const handleSave = async () => {
  if (!modalForm.name) {
    alert('请输入分类名称')
    return
  }
  
  try {
    if (editingCategory.value) {
      await categoryApi.updateCategory(editingCategory.value.id, modalForm)
    } else {
      await categoryApi.createCategory(modalForm)
    }
    showModal.value = false
    editingCategory.value = null
    modalForm.name = ''
    modalForm.slug = ''
    modalForm.description = ''
    await fetchCategories()
  } catch (error) {
    console.error('Failed to save category:', error)
    alert('保存失败')
  }
}

onMounted(() => {
  fetchCategories()
})
</script>
