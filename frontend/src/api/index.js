import axios from 'axios'

const API_BASE_URL = '/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      localStorage.removeItem('role')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const authApi = {
  login: (data) => api.post('/auth/login', data)
}

export const articleApi = {
  getArticles: (params) => api.get('/articles', { params }),
  getArticleById: (id) => api.get(`/articles/${id}`),
  getArticleBySlug: (slug) => api.get(`/articles/slug/${slug}`),
  searchArticles: (params) => api.get('/articles/search', { params }),
  createArticle: (data) => api.post('/admin/articles', data),
  updateArticle: (id, data) => api.put(`/admin/articles/${id}`, data),
  deleteArticle: (id) => api.delete(`/admin/articles/${id}`)
}

export const categoryApi = {
  getCategories: () => api.get('/categories'),
  getCategoryById: (id) => api.get(`/categories/${id}`),
  createCategory: (data) => api.post('/admin/categories', data),
  updateCategory: (id, data) => api.put(`/admin/categories/${id}`, data),
  deleteCategory: (id) => api.delete(`/admin/categories/${id}`)
}

export const tagApi = {
  getTags: () => api.get('/tags'),
  getTagById: (id) => api.get(`/tags/${id}`),
  createTag: (data) => api.post('/admin/tags', data),
  updateTag: (id, data) => api.put(`/admin/tags/${id}`, data),
  deleteTag: (id) => api.delete(`/admin/tags/${id}`)
}

export const commentApi = {
  getComments: (articleId) => api.get(`/articles/${articleId}/comments`),
  getAllComments: () => api.get('/admin/comments'),
  createComment: (articleId, data) => api.post(`/articles/${articleId}/comments`, data),
  approveComment: (id) => api.put(`/admin/comments/${id}/approve`),
  deleteComment: (id) => api.delete(`/admin/comments/${id}`)
}
