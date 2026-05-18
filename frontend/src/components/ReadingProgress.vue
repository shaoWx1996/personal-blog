<template>
  <div class="fixed top-0 left-0 right-0 h-1 z-[60] bg-transparent">
    <div
      class="h-full bg-gradient-to-r from-neon-blue via-neon-purple to-neon-cyan transition-all duration-150"
      :style="{ width: progress + '%' }"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const progress = ref(0)

const updateProgress = () => {
  const scrollTop = window.scrollY
  const docHeight = document.documentElement.scrollHeight - window.innerHeight
  progress.value = docHeight > 0 ? (scrollTop / docHeight) * 100 : 0
}

onMounted(() => {
  window.addEventListener('scroll', updateProgress, { passive: true })
  updateProgress()
})

onUnmounted(() => {
  window.removeEventListener('scroll', updateProgress)
})
</script>
