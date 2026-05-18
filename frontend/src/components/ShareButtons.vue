<template>
  <div class="flex items-center space-x-3">
    <span class="text-sm" :class="isDark ? 'text-gray-400' : 'text-gray-600'">
      分享到:
    </span>
    
    <!-- 微信 -->
    <button 
      @click="shareToWechat"
      class="p-2 rounded-full transition-all duration-300 hover:scale-110"
      :class="isDark ? 'bg-green-500/20 text-green-400 hover:bg-green-500/30' : 'bg-green-50 text-green-600 hover:bg-green-100'"
      title="分享到微信"
    >
      <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
        <path d="M8.691 2.188C3.891 2.188 0 5.476 0 9.53c0 2.212 1.17 4.203 3.002 5.55a.59.59 0 01.213.665l-.39 1.48c-.019.07-.048.141-.048.213 0 .163.13.295.29.295a.326.326 0 00.167-.054l1.903-1.114a.864.864 0 01.717-.098 10.16 10.16 0 002.837.403c.276 0 .543-.027.811-.05-.857-2.578.157-4.972 1.932-6.446 1.703-1.415 3.882-1.98 5.853-1.838-.576-3.583-4.196-6.348-8.596-6.348zM5.785 5.991c.642 0 1.162.529 1.162 1.18a1.17 1.17 0 01-1.162 1.178A1.17 1.17 0 014.623 7.17c0-.651.52-1.18 1.162-1.18zm5.813 0c.642 0 1.162.529 1.162 1.18a1.17 1.17 0 01-1.162 1.178 1.17 1.17 0 01-1.162-1.178c0-.651.52-1.18 1.162-1.18zm5.34 2.867c-1.797-.052-3.746.512-5.28 1.786-1.72 1.428-2.687 3.72-1.78 6.22.942 2.453 3.666 4.229 6.884 4.229.826 0 1.622-.12 2.361-.336a.722.722 0 01.598.082l1.584.926a.272.272 0 00.14.047c.134 0 .24-.111.24-.247 0-.06-.023-.12-.038-.177l-.327-1.233a.582.582 0 01-.023-.156.49.49 0 01.201-.398C23.024 18.48 24 16.82 24 14.98c0-3.21-2.931-5.837-6.656-6.088V8.89c-.135-.01-.269-.03-.407-.032zm-2.53 3.274c.535 0 .969.44.969.982a.976.976 0 01-.969.983.976.976 0 01-.969-.983c0-.542.434-.982.97-.982zm4.844 0c.535 0 .969.44.969.982a.976.976 0 01-.969.983.976.976 0 01-.969-.983c0-.542.434-.982.969-.982z"/>
      </svg>
    </button>
    
    <!-- 微博 -->
    <button 
      @click="shareToWeibo"
      class="p-2 rounded-full transition-all duration-300 hover:scale-110"
      :class="isDark ? 'bg-red-500/20 text-red-400 hover:bg-red-500/30' : 'bg-red-50 text-red-600 hover:bg-red-100'"
      title="分享到微博"
    >
      <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
        <path d="M10.098 20.323c-3.977.391-7.414-1.406-7.672-4.02-.259-2.609 2.759-5.047 6.74-5.441 3.979-.394 7.413 1.404 7.671 4.018.259 2.6-2.759 5.049-6.737 5.439h-.002zm6.714-7.501c-.387-.993-1.089-1.746-2.087-2.246-1.396-.697-3.101-.598-4.77-.305-.295.052-.589.112-.882.17-.025.01-.058.021-.099.036-.118.041-.248.094-.312.102-.094.011-.196-.023-.275-.112a.394.394 0 01-.099-.287c.021-.244.104-.479.177-.706.106-.333.216-.665.263-.973.083-.533.011-1.028-.205-1.488-.387-.818-1.118-1.415-1.982-1.722-2.296-.817-5.439-.076-7.18 1.603-1.74 1.679-2.138 4.177-1.098 6.326.39.807.953 1.41 1654 1.855.14.089.224.244.203.413-.017.138-.087.263-.199.347-1.23.911-2.142 2.017-2.726 3.3-.453.994-.59 2.062-.39 3.099.4 2.075 1.841 3.532 3.893 3.932 2.76.539 5.439-.879 6.437-3.055.45-.982.558-2.085.29-3.152-.263-1.052-.89-1.905-1.802-2.51a1.374 1.374 0 00-.29-.171l-.002.001c-.12-.059-.268-.059-.385-.015-.015.004-.035.006-.049.011-1.005.325-2.063.63-3.134.653-1.413.03-2.657-.507-3.53-1.548-.9-1.073-1.156-2.522-.65-3.832.11-.285.252-.553.405-.815.11-.188.223-.374.327-.564.117-.215.281-.314.509-.346a.63.63 0 01.139-.014c1.596-.107 3.17-.214 4.69.16.732.181 1.424.462 2.05.868.67.434 1.17 1.015 1.538 1.699.44.819.57 1.709.398 2.612l.001.002c.017.037.025.075.038.112l.007-.001z"/>
      </svg>
    </button>
    
    <!-- Twitter -->
    <button 
      @click="shareToTwitter"
      class="p-2 rounded-full transition-all duration-300 hover:scale-110"
      :class="isDark ? 'bg-blue-500/20 text-blue-400 hover:bg-blue-500/30' : 'bg-blue-50 text-blue-600 hover:bg-blue-100'"
      title="分享到 Twitter"
    >
      <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
        <path d="M23.953 4.57a10 10 0 01-2.825.775 4.958 4.958 0 002.163-2.723c-.951.555-2.005.959-3.127 1.184a4.92 4.92 0 00-8.384 4.482C7.69 8.095 4.067 6.13 1.64 3.162a4.822 4.822 0 00-.666 2.475c0 1.71.87 3.213 2.188 4.096a4.904 4.904 0 01-2.228-.616v.06a4.923 4.923 0 003.946 4.827 4.996 4.996 0 01-2.212.085 4.936 4.936 0 004.604 3.417 9.867 9.867 0 01-6.102 2.105c-.39 0-.779-.023-1.17-.067a13.995 13.995 0 007.557 2.209c9.053 0 13.998-7.496 13.998-13.985 0-.21 0-.42-.015-.63A9.935 9.935 0 0024 4.59z"/>
      </svg>
    </button>
    
    <!-- 复制链接 -->
    <button 
      @click="copyLink"
      class="p-2 rounded-full transition-all duration-300 hover:scale-110"
      :class="isDark ? 'bg-gray-500/20 text-gray-400 hover:bg-gray-500/30' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'"
      title="复制链接"
    >
      <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"/>
      </svg>
    </button>
    
    <!-- 复制成功提示 -->
    <transition
      enter-active-class="transition-all duration-300"
      enter-from-class="opacity-0 transform -translate-y-2"
      enter-to-class="opacity-100 transform translate-y-0"
      leave-active-class="transition-all duration-300"
      leave-from-class="opacity-100 transform translate-y-0"
      leave-to-class="opacity-0 transform -translate-y-2"
    >
      <span 
        v-if="showCopied"
        class="absolute -top-8 left-1/2 transform -translate-x-1/2 px-3 py-1 text-xs rounded-full whitespace-nowrap"
        :class="isDark ? 'bg-neon-blue text-white' : 'bg-blue-500 text-white'"
      >
        已复制到剪贴板!
      </span>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useThemeStore } from '../stores/theme'

const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  url: {
    type: String,
    default: ''
  }
})

const themeStore = useThemeStore()
const isDark = computed(() => themeStore.isDark)
const showCopied = ref(false)

const getShareUrl = () => {
  return props.url || window.location.href
}

const shareToWechat = () => {
  alert('请使用微信扫一扫分享此页面\n\n打开微信 → 扫一扫 → 将此页面对准扫描')
}

const shareToWeibo = () => {
  const shareUrl = `https://service.weibo.com/share/share.php?url=${encodeURIComponent(getShareUrl())}&title=${encodeURIComponent(props.title)}`
  window.open(shareUrl, '_blank', 'width=600,height=400')
}

const shareToTwitter = () => {
  const shareUrl = `https://twitter.com/intent/tweet?text=${encodeURIComponent(props.title)}&url=${encodeURIComponent(getShareUrl())}`
  window.open(shareUrl, '_blank', 'width=600,height=400')
}

const copyLink = async () => {
  try {
    await navigator.clipboard.writeText(getShareUrl())
    showCopied.value = true
    setTimeout(() => {
      showCopied.value = false
    }, 2000)
  } catch (err) {
    // Fallback for older browsers
    const textarea = document.createElement('textarea')
    textarea.value = getShareUrl()
    document.body.appendChild(textarea)
    textarea.select()
    document.execCommand('copy')
    document.body.removeChild(textarea)
    showCopied.value = true
    setTimeout(() => {
      showCopied.value = false
    }, 2000)
  }
}
</script>

<style scoped>
button {
  position: relative;
}
</style>
