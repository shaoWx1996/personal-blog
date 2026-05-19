<template>
  <button
    v-if="isVisible"
    @click="scrollToTop"
    class="fixed bottom-8 right-8 z-50 p-4 rounded-full glass neon-glow-hover transition-all duration-300 hover:scale-110 animate-fade-in-up"
    title="回到顶部"
  >
    <svg 
      class="w-6 h-6 transition-transform duration-300" 
      :class="{ 'rotate-[-45deg]': isLaunching }"
      fill="none" 
      stroke="currentColor" 
      viewBox="0 0 24 24"
      :class="isDark ? 'text-white' : 'text-gray-800'"
    >
      <path 
        stroke-linecap="round" 
        stroke-linejoin="round" 
        stroke-width="2" 
        d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8"
      />
    </svg>
  </button>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
import { useThemeStore } from '../stores/theme'
import { useScroll } from '@vueuse/core'

const themeStore = useThemeStore()
const isDark = computed(() => themeStore.isDark)
const isVisible = ref(false)
const isLaunching = ref(false)

const { y } = useScroll()

const handleScroll = () => {
  isVisible.value = y.value > 300
}

const scrollToTop = () => {
  isLaunching.value = true
  window.scrollTo({ top: 0, behavior: 'smooth' })
  setTimeout(() => {
    isLaunching.value = false
  }, 1000)
}

onMounted(() => {
  const unwatch = watch(y, handleScroll)
  onUnmounted(unwatch)
})
</script>
