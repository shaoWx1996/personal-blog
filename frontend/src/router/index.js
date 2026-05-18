import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/article/:slug',
    name: 'ArticleDetail',
    component: () => import('../views/ArticleDetail.vue')
  },
  {
    path: '/category/:slug',
    name: 'Category',
    component: () => import('../views/Category.vue')
  },
  {
    path: '/tag/:slug',
    name: 'Tag',
    component: () => import('../views/Tag.vue')
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('../views/Search.vue')
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/admin/Admin.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/articles',
    name: 'AdminArticles',
    component: () => import('../views/admin/ArticleList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/articles/new',
    name: 'AdminArticleNew',
    component: () => import('../views/admin/ArticleForm.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/articles/:id/edit',
    name: 'AdminArticleEdit',
    component: () => import('../views/admin/ArticleForm.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/categories',
    name: 'AdminCategories',
    component: () => import('../views/admin/CategoryList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/tags',
    name: 'AdminTags',
    component: () => import('../views/admin/TagList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/comments',
    name: 'AdminComments',
    component: () => import('../views/admin/CommentList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token')
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router
