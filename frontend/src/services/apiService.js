import axios from 'axios';

const API_BASE_URL = "http://localhost:8080";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  }
});

api.interceptors.response.use(
  response => response,
  error => {
    return Promise.reject(error);
  }
);

const apiService = {
  getAllPessoas: () => api.get('/pessoa'),
  createPessoa: (pessoaData) => api.post('/pessoa', pessoaData),
  updatePessoa: (id, pessoaData) => api.put(`/pessoa/${id}`, pessoaData),
  getPessoaById: (id) => api.get(`/pessoa/${id}`),
  deletePessoa: (id) => api.delete(`/pessoa/${id}`),
};