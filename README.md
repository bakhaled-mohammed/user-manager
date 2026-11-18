# User Manager — projet d'entraînement DevOps

Mini CRUD d'utilisateurs (ajouter, afficher, modifier, supprimer) qui sert de support
pour pratiquer Git/GitHub, Docker, CI/CD, Jenkins et Kubernetes.

| Partie   | Technologie                         | Port |
|----------|-------------------------------------|------|
| backend  | Spring Boot 3.5, Java 21, JPA       | 8080 |
| frontend | React 18 + Vite                     | 3000 |
| base     | MySQL (base `user_manager`)         | 3306 |

## API

| Méthode | URL                 | Rôle                     |
|---------|---------------------|--------------------------|
| GET     | /api/users          | Lister les utilisateurs  |
| GET     | /api/users/{id}     | Un utilisateur           |
| POST    | /api/users          | Ajouter                  |
| PUT     | /api/users/{id}     | Modifier                 |
| DELETE  | /api/users/{id}     | Supprimer                |
| GET     | /actuator/health    | Santé de l'application   |

Corps JSON : `{"nom": "Alami", "prenom": "Ali", "email": "ali@mail.com"}`

## Lancer en local (sans Docker)

1. Démarrer MySQL (la base `user_manager` est créée automatiquement).
2. Backend : ouvrir `backend` dans IntelliJ et lancer `UserManagerApplication`.
3. Frontend :
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
4. Ouvrir http://localhost:3000

## Tests

Les tests utilisent une base H2 en mémoire : ils n'ont pas besoin de MySQL.

```bash
cd backend
mvn test
```

## Variables d'environnement du backend

`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
