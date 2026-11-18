// Toutes les requêtes vers le backend. Chemin relatif : fonctionne en dev (proxy Vite) et en production (Nginx).
const BASE_URL = '/api/users'

async function request(url, options = {}) {
  const res = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  })
  if (res.status === 204) return null
  const data = await res.json().catch(() => null)
  if (!res.ok) {
    const error = new Error(data?.message || `Erreur HTTP ${res.status}`)
    error.details = data?.details || {}
    throw error
  }
  return data
}

export const getUsers = () => request(BASE_URL)
export const createUser = (user) => request(BASE_URL, { method: 'POST', body: JSON.stringify(user) })
export const updateUser = (id, user) => request(`${BASE_URL}/${id}`, { method: 'PUT', body: JSON.stringify(user) })
export const deleteUser = (id) => request(`${BASE_URL}/${id}`, { method: 'DELETE' })
