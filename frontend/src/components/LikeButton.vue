<template>
  <button 
    class="relative flex items-center space-x-2 px-4 py-2 rounded-full transition-all duration-300"
    :class="[
      isLiked 
        ? 'bg-red-500/20 text-red-500 border border-red-500/30' 
        : isDark 
          ? 'bg-dark-border text-gray-400 hover:text-red-400' 
          : 'bg-gray-100 text-gray-600 hover:text-red-500'
    ]"
    @click="handleLike"
  >
    <div class="relative">
      <svg 
        class="w-5 h-5 transition-transform duration-300"
        :class="{ 'scale-125': isAnimating }"
        :fill="isLiked ? 'currentColor' : 'none'"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path 
          stroke-linecap="round" 
          stroke-linejoin="round" 
          stroke-width="2" 
          d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
        />
      </svg>
      
      <!-- 粒子效果 -->
      <canvas 
        v-if="showParticles"
        ref="particleCanvas"
        class="absolute inset-0 pointer-events-none"
        width="40"
        height="40"
        style="transform: translate(-50%, -50%); left: 50%; top: 50%;"
      />
    </div>
    
    <span class="font-medium">{{ formattedCount }}</span>
  </button>
</template>

<script setup>
import { ref, computed, nextTick } from 'vue'
import { useThemeStore } from '../stores/theme'

const props = defineProps({
  count: {
    type: Number,
    default: 0
  },
  articleId: {
    type: [Number, String],
    required: true
  }
})

const themeStore = useThemeStore()
const isDark = computed(() => themeStore.isDark)

const isLiked = ref(false)
const isAnimating = ref(false)
const showParticles = ref(false)
const particleCanvas = ref(null)
const currentCount = ref(props.count)

const formattedCount = computed(() => {
  if (currentCount.value >= 1000) {
    return (currentCount.value / 1000).toFixed(1) + 'k'
  }
  return currentCount.value
})

const handleLike = async () => {
  if (isAnimating.value) return
  
  isAnimating.value = true
  
  if (!isLiked.value) {
    // 点赞
    isLiked.value = true
    currentCount.value++
    showParticles.value = true
    
    await nextTick()
    createParticleExplosion()
    
    setTimeout(() => {
      showParticles.value = false
    }, 1000)
  } else {
    // 取消点赞
    isLiked.value = false
    currentCount.value--
  }
  
  setTimeout(() => {
    isAnimating.value = false
  }, 300)
}

const createParticleExplosion = () => {
  const canvas = particleCanvas.value
  if (!canvas) return
  
  const ctx = canvas.getContext('2d')
  const particles = []
  const particleCount = 12
  
  for (let i = 0; i < particleCount; i++) {
    particles.push({
      x: 20,
      y: 20,
      vx: (Math.random() - 0.5) * 8,
      vy: (Math.random() - 0.5) * 8,
      life: 1,
      color: `hsl(${Math.random() * 60 + 330}, 100%, 60%)`
    })
  }
  
  const animate = () => {
    ctx.clearRect(0, 0, 40, 40)
    
    let hasLivingParticles = false
    
    particles.forEach(p => {
      if (p.life > 0) {
        hasLivingParticles = true
        p.x += p.vx
        p.y += p.vy
        p.life -= 0.02
        
        ctx.beginPath()
        ctx.arc(p.x, p.y, 2 * p.life, 0, Math.PI * 2)
        ctx.fillStyle = p.color
        ctx.globalAlpha = p.life
        ctx.fill()
      }
    })
    
    if (hasLivingParticles) {
      requestAnimationFrame(animate)
    }
  }
  
  animate()
}
</script>
