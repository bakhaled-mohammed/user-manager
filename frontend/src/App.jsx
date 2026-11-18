import { useEffect, useState } from 'react'
import { createUser, deleteUser, getUsers, updateUser } from './api.js'

const EMPTY_FORM = { nom: '', prenom: '', email: '' }

export default function App() {
  const [users, setUsers] = useState([])
  const [form, setForm] = useState(EMPTY_FORM)
  const [editingId, setEditingId] = useState(null)
  const [errors, setErrors] = useState({})
  const [message, setMessage] = useState(null)
  const [loading, setLoading] = useState(true)

  async function loadUsers() {
    try {
      setUsers(await getUsers())
    } catch (e) {
      setMessage({ type: 'error', text: "Impossible de joindre le backend. Vérifiez que Spring Boot tourne sur le port 8080." })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadUsers() }, [])

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  function resetForm() {
    setForm(EMPTY_FORM)
    setEditingId(null)
    setErrors({})
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setErrors({})
    try {
      if (editingId) {
        await updateUser(editingId, form)
        setMessage({ type: 'success', text: 'Utilisateur modifié.' })
      } else {
        await createUser(form)
        setMessage({ type: 'success', text: 'Utilisateur ajouté.' })
      }
      resetForm()
      loadUsers()
    } catch (err) {
      setErrors(err.details)
      setMessage({ type: 'error', text: err.message })
    }
  }

  function handleEdit(user) {
    setForm({ nom: user.nom, prenom: user.prenom, email: user.email })
    setEditingId(user.id)
    setErrors({})
    setMessage(null)
  }

  async function handleDelete(user) {
    if (!window.confirm(`Supprimer ${user.prenom} ${user.nom} ?`)) return
    try {
      await deleteUser(user.id)
      setMessage({ type: 'success', text: 'Utilisateur supprimé.' })
      if (editingId === user.id) resetForm()
      loadUsers()
    } catch (err) {
      setMessage({ type: 'error', text: err.message })
    }
  }

  return (
    <main className="page">
      <header>
        <h1>Gestion des utilisateurs</h1>
        <p className="subtitle">Spring Boot · React · MySQL — projet d'entraînement DevOps</p>
      </header>

      <section className="card">
        <h2>{editingId ? 'Modifier un utilisateur' : 'Ajouter un utilisateur'}</h2>
        <form onSubmit={handleSubmit} noValidate>
          {['nom', 'prenom', 'email'].map((field) => (
            <label key={field}>
              {field === 'prenom' ? 'Prénom' : field === 'nom' ? 'Nom' : 'Email'}
              <input
                name={field}
                type={field === 'email' ? 'email' : 'text'}
                value={form[field]}
                onChange={handleChange}
                className={errors[field] ? 'invalid' : ''}
              />
              {errors[field] && <span className="field-error">{errors[field]}</span>}
            </label>
          ))}
          <div className="actions">
            <button type="submit">{editingId ? 'Enregistrer' : 'Ajouter'}</button>
            {editingId && <button type="button" className="secondary" onClick={resetForm}>Annuler</button>}
          </div>
        </form>
        {message && <p className={`message ${message.type}`}>{message.text}</p>}
      </section>

      <section className="card">
        <h2>Utilisateurs ({users.length})</h2>
        {loading ? (
          <p className="muted">Chargement…</p>
        ) : users.length === 0 ? (
          <p className="muted">Aucun utilisateur. Ajoutez-en un avec le formulaire.</p>
        ) : (
          <table>
            <thead>
              <tr><th>#</th><th>Nom</th><th>Prénom</th><th>Email</th><th></th></tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id} className={editingId === user.id ? 'editing' : ''}>
                  <td>{user.id}</td>
                  <td>{user.nom}</td>
                  <td>{user.prenom}</td>
                  <td>{user.email}</td>
                  <td className="row-actions">
                    <button className="secondary" onClick={() => handleEdit(user)}>Modifier</button>
                    <button className="danger" onClick={() => handleDelete(user)}>Supprimer</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </main>
  )
}
