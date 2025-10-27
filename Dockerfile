# Usa una imagen base de Java
FROM openjdk:21-ea-24-oracle

# Crea un directorio para la app
WORKDIR /app

# Copia el jar generado al contenedor
COPY target/*.jar app.jar

# Expone el puerto 8080 (puerto por defecto de Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]