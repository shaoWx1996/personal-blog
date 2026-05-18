<template>
  <div class="bg-white rounded-lg shadow-md p-6">
    <h3 class="text-lg font-bold text-gray-800 mb-4">标签</h3>
    <div class="flex flex-wrap gap-2">
      <router-link 
        v-for="tag in tags" 
        :key="tag.id"
        :to="`/tag/${tag.slug}`"
        class="px-3 py-1 text-sm bg-gray-100 text-gray-600 rounded-full hover:bg-blue-100 hover:text-blue-600 transition-colors"
      >
        {{ tag.name }}
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { tagApi } from '../api'

const tags = ref([])

onMounted(async () => {
  try {
    const response = await tagApi.getTags()
    tags.value = response.data
  } catch (error) {
    console.error('Failed to fetch tags:', error)
  }
})
</script>
