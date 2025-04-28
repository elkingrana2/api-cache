# Dockerfile
FROM eclipse-temurin:21-jdk-jammy as build

# Copia el jar generado por Spring Boot
WORKDIR /app

# Copiar el jar (cambia 'your-app.jar' por el nombre de tu jar final)
COPY target/cache-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
