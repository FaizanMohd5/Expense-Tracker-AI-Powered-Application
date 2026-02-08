import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080',
});

// This automatically attaches your JWT token to every request if it exists
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Auth Endpoints
export const register = (data) => api.post('/auth/register', data);
export const login = (data) => api.post('/auth/login', data);

// User Profile Endpoints
export const getProfile = () => api.get('/users/profile');
export const updateProfile = (data) => api.put('/users/profile', data);

// Expense Endpoints
export const getExpenses = (params) => api.get('/expenses', { params });
export const addExpense = (data) => api.post('/expenses', data);
export const editExpense = (id, data) => api.put(`/expenses/${id}`, data);
export const deleteExpense = (id) => api.delete(`/expenses/${id}`);

// Category Endpoints
export const getCategories = () => api.get('/categories');
export const addCategory = (data) => api.post('/categories', data);

// Summary Endpoint
export const getMonthlySummary = (params) => api.get('/expenses/summary/monthly', { params });

export default api;