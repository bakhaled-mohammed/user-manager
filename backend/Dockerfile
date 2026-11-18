# ---- Étape 1 : compiler le projet avec Maven ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B package -DskipTests

# ---- Étape 2 : image légère pour exécuter le .jar ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/user-manager.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]