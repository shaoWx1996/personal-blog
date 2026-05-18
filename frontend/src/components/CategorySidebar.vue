<template>
  <div class="bg-white rounded-lg shadow-md p-6">
    <h3 class="text-lg font-bold text-gray-800 mb-4">分类</h3>
    <ul class="space-y-2">
      <li v-for="category in categories" :key="category.id">
        <router-link 
          :to="`/category/${category.slug}`"
          class="flex items-center justify-between text-gray-600 hover:text-blue-600 transition-colors py-2 border-b border-gray-100"
        >
          <span>{{ category.name }}</span>
          <span class="text-sm text-gray-400">{{ categoryCount[category.id] || 0 }}</span>
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { categoryApi } from '../api'

const categories = ref([])
const categoryCount = ref({})

onMounted(async () => {
  try {
    const response = await categoryApi.getCategories()
    categories.value = response.data
  } catch (error) {
    console.error('Failed to fetch categories:', error)
  }
})
</script>
