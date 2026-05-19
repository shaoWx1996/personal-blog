<template>
  <div class="min-h-screen bg-gray-50">
    <router-view />
    <BackToTop />
  </div>
</template>

<script setup>
import BackToTop from './components/BackToTop.vue'
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
</style>
