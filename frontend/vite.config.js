import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// En développement, les appels /api sont envoyés à Spring Boot (port 8080).
// Avec Docker et Kubernetes, c'est Nginx qui fera ce travail.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      '/api': 'http://localhost:8080'
    }
  }
})
