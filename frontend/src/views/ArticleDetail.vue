<template>
  <div class="min-h-screen">
    <Header />
    
    <main class="max-w-4xl mx-auto px-4 py-8">
      <article v-if="article" class="bg-white rounded-lg shadow-md overflow-hidden">
        <div v-if="article.coverImage" class="h-64 md:h-80">
          <img :src="article.coverImage" :alt="article.title" class="w-full h-full object-cover" />
        </div>
        
        <div class="p-8">
          <div class="flex items-center space-x-4 mb-4">
            <span 
              v-if="article.category"
              class="px-3 py-1 text-sm bg-blue-100 text-blue-600 rounded-full"
            >
              {{ article.category.name }}
            </span>
            <span class="text-gray-500">{{ formatDate(article.createdAt) }}</span>
          </div>
          
          <h1 class="text-3xl font-bold text-gray-800 mb-4">{{ article.title }}</h1>
          
          <div class="flex flex-wrap gap-2 mb-6">
            <span 
              v-for="tag in article.tags" 
              :key="tag.id"
              class="px-3 py-1 text-sm bg-gray-100 text-gray-600 rounded-full"
            >
              {{ tag.name }}
            </span>
          </div>
          
          <div class="flex items-center space-x-6 mb-6 text-gray-500">
            <span>{{ article.viewCount }} 阅读</span>
            <span>{{ article.likeCount }} 点赞</span>
            <span>{{ article.commentCount }} 评论</span>
          </div>
          
          <div class="markdown-content" v-html="renderedContent"></div>
        </div>
      </article>
      
      <div class="mt-8 space-y-6">
        <CommentForm :article-id="article?.id" @comment-created="loadComments" />
        <CommentList :comments="comments" />
      </div>
    </main>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'
import CommentForm from '../components/CommentForm.vue'
import CommentList from '../components/CommentList.vue'
import { articleApi, commentApi } from '../api'
import MarkdownIt from 'markdown-it'

const route = useRoute()
const article = ref(null)
const comments = ref([])

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true
})

const renderedContent = computed(() => {
  if (!article.value?.content) return ''
  return md.render(article.value.content)
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const loadArticle = async () => {
  const slug = route.params.slug
  try {
    const response = await articleApi.getArticleBySlug(slug)
    article.value = response.data
  } catch (error) {
    console.error('Failed to fetch article:', error)
  }
}

const loadComments = async () => {
  if (article.value?.id) {
    try {
      const response = await commentApi.getComments(article.value.id)
      comments.value = response.data
    } catch (error) {
      console.error('Failed to fetch comments:', error)
    }
  }
}

onMounted(() => {
  loadArticle().then(() => {
    loadComments()
  })
})
</script>
