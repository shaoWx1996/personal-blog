<template>
  <div class="min-h-screen bg-gray-100">
    <div class="flex">
      <nav class="w-64 bg-gray-800 text-white min-h-screen">
        <div class="p-4 border-b border-gray-700">
          <h1 class="text-lg font-bold">博客管理</h1>
        </div>
        
        <ul class="p-4 space-y-2">
          <li>
            <router-link 
              to="/admin/articles" 
              class="block px-4 py-2 rounded-lg hover:bg-gray-700 transition-colors"
              :class="{ 'bg-gray-700': currentRoute.startsWith('/admin/articles') }"
            >
              文章管理
            </router-link>
          </li>
          <li>
            <router-link 
              to="/admin/categories" 
              class="block px-4 py-2 rounded-lg hover:bg-gray-700 transition-colors"
              :class="{ 'bg-gray-700': currentRoute === '/admin/categories' }"
            >
              分类管理
            </router-link>
          </li>
          <li>
            <router-link 
              to="/admin/tags" 
              class="block px-4 py-2 rounded-lg hover:bg-gray-700 transition-colors"
              :class="{ 'bg-gray-700': currentRoute === '/admin/tags' }"
            >
              标签管理
            </router-link>
          </li>
          <li>
            <router-link 
              to="/admin/comments" 
              class="block px-4 py-2 rounded-lg hover:bg-gray-700 transition-colors"
              :class="{ 'bg-gray-700': currentRoute === '/admin/comments' }"
            >
              评论管理
            </router-link>
          </li>
        </ul>
        
        <div class="absolute bottom-0 p-4 w-64">
          <button
            @click="handleLogout"
            class="w-full px-4 py-2 text-left text-gray-400 hover:text-white hover:bg-gray-700 rounded-lg transition-colors"
          >
            退出登录
          </button>
        </div>
      </nav>
      
      <main class="flex-1 p-8">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../../store/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const currentRoute = computed(() => route.fullPath)

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

const checkAuth = () => {
  if (!authStore.isAuthenticated) {
    router.push('/login')
  }
}

onMounted(() => {
  checkAuth()
})
</script>
