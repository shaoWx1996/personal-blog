<template>
  <div class="min-h-screen">
    <Header />
    
    <main class="max-w-4xl mx-auto px-4 py-8">
      <div class="flex flex-col lg:flex-row gap-8">
        <div class="flex-1">
          <div class="mb-8">
            <h2 class="text-2xl font-bold text-gray-800 mb-2">标签：{{ tagName }}</h2>
            <p class="text-gray-500">共 {{ totalElements }} 篇文章</p>
          </div>
          
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <ArticleCard 
              v-for="article in articles" 
              :key="article.id" 
              :article="article" 
            />
          </div>
          
          <div v-if="totalPages > 1" class="mt-8 flex justify-center">
            <nav class="flex items-center space-x-2">
              <button
                @click="goToPage(currentPage - 1)"
                :disabled="currentPage === 0"
                class="px-4 py-2 border border-gray-300 rounded-lg disabled:opacity-50 hover:bg-gray-50"
              >
                上一页
              </button>
              <span class="px-4 py-2 text-gray-600">
                第 {{ currentPage + 1 }} / {{ totalPages }} 页
              </span>
              <button
                @click="goToPage(currentPage + 1)"
                :disabled="currentPage >= totalPages - 1"
                class="px-4 py-2 border border-gray-300 rounded-lg disabled:opacity-50 hover:bg-gray-50"
              >
                下一页
              </button>
            </nav>
          </div>
        </div>
        
        <aside class="lg:w-80 space-y-6">
          <CategorySidebar />
          <TagCloud />
          <RecentArticles />
        </aside>
      </div>
    </main>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'
import ArticleCard from '../components/ArticleCard.vue'
import CategorySidebar from '../components/CategorySidebar.vue'
import TagCloud from '../components/TagCloud.vue'
import RecentArticles from '../components/RecentArticles.vue'
import { articleApi, tagApi } from '../api'

const route = useRoute()
const articles = ref([])
const currentPage = ref(0)
const totalPages = ref(0)
const totalElements = ref(0)
const tagName = ref('')
const tagId = ref(null)

const fetchTag = async (slug) => {
  try {
    const response = await tagApi.getTags()
    const tag = response.data.find(t => t.slug === slug)
    if (tag) {
      tagName.value = tag.name
      tagId.value = tag.id
      fetchArticles(0)
    }
  } catch (error) {
    console.error('Failed to fetch tag:', error)
  }
}

const fetchArticles = async (page = 0) => {
  if (!tagId.value) return
  try {
    const response = await articleApi.getArticles({ page, size: 6, tagId: tagId.value })
    articles.value = response.data.content
    totalPages.value = response.data.totalPages
    totalElements.value = response.data.totalElements
    currentPage.value = page
  } catch (error) {
    console.error('Failed to fetch articles:', error)
  }
}

const goToPage = (page) => {
  if (page >= 0 && page < totalPages.value) {
    fetchArticles(page)
  }
}

onMounted(() => {
  fetchTag(route.params.slug)
})

watch(() => route.params.slug, (newSlug) => {
  fetchTag(newSlug)
})
</script>
