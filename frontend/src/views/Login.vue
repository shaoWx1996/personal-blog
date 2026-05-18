<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="bg-white rounded-lg shadow-lg p-8 w-full max-w-md">
      <h2 class="text-2xl font-bold text-center text-gray-800 mb-6">登录</h2>
      
      <form @submit.prevent="handleLogin">
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2">用户名</label>
          <input
            v-model="form.username"
            type="text"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
            placeholder="请输入用户名"
          />
        </div>
        
        <div class="mb-6">
          <label class="block text-sm font-medium text-gray-700 mb-2">密码</label>
          <input
            v-model="form.password"
            type="password"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
            placeholder="请输入密码"
          />
        </div>
        
        <button
          type="submit"
          :disabled="submitting"
          class="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
        >
          {{ submitting ? '登录中...' : '登录' }}
        </button>
        
        <p v-if="error" class="mt-4 text-sm text-red-600 text-center">{{ error }}</p>
      </form>
      
      <p class="mt-6 text-center text-sm text-gray-500">
        默认账号：admin / admin
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'
import { authApi } from '../api'

const router = useRouter()
const authStore = useAuthStore()

const form = reactive({
  username: '',
  password: ''
})

const submitting = ref(false)
const error = ref('')

const handleLogin = async () => {
  if (!form.username || !form.password) {
    error.value = '请填写用户名和密码'
    return
  }
  
  submitting.value = true
  error.value = ''
  
  try {
    console.log('登录请求:', form)
    const response = await authApi.login(form)
    console.log('登录响应:', response)
    authStore.login(response.data)
    console.log('Store状态:', { 
      token: authStore.token, 
      username: authStore.username, 
      isAuthenticated: authStore.isAuthenticated 
    })
    router.push('/admin')
  } catch (err) {
    console.error('登录失败:', err)
    if (err.response) {
      console.error('响应状态:', err.response.status)
      console.error('响应数据:', err.response.data)
      error.value = err.response.data?.message || '登录失败'
    } else {
      error.value = '网络错误，请检查后端服务是否运行'
    }
  } finally {
    submitting.value = false
  }
}
</script>
