<template>
  <div 
    class="prose prose-invert max-w-none"
    v-html="renderedContent"
  />
</template>

<script setup>
import { computed } from 'vue'
import { marked } from 'marked'
import hljs from 'highlight.js'

const props = defineProps({
  content: {
    type: String,
    required: true
  }
})

marked.setOptions({
  highlight: function(code, lang) {
    if (lang && hljs.getLanguage(lang)) {
      return hljs.highlight(code, { language: lang }).value
    }
    return code
  },
  breaks: true,
  gfm: true
})

const renderedContent = computed(() => {
  return marked(props.content)
})
</script>

<style>
pre {
  background-color: #1e293b;
  border-radius: 0.5rem;
  padding: 1rem;
  overflow-x: auto;
}

code {
  font-family: 'Fira Code', monospace;
}

pre code {
  background-color: transparent;
  padding: 0;
}

:not(pre) > code {
  background-color: #334155;
  padding: 0.2rem 0.4rem;
  border-radius: 0.25rem;
  font-size: 0.875em;
}

.hljs {
  background: transparent;
  color: #e2e8f0;
}

.hljs-keyword { color: #c678dd; }
.hljs-string { color: #98c379; }
.hljs-number { color: #d19a66; }
.hljs-comment { color: #5c6370; font-style: italic; }
.hljs-function { color: #61afef; }
</style>
