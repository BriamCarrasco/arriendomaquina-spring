# Etapa 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Run
FROM eclipse-temurin:21-jre
WORKDIR /app

# Crear usuario no root para seguridad
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
USER appuser

COPY --from=build /app/target/*.jar app.jar

# Copia el properties desde el contexto del proyecto
COPY src/main/resources/application.properties ./application.properties

# Variables de entorno (ajusta DB_URL si usas contenedor DB)
ENV JWT_SECRET_KEY="XDWDH3u4tdxebqBp2Iesst01kCurg10QegORWr3igpt"
ENV DB_URL="jdbc:mysql://host.docker.internal:3306/arriendo_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Santiago"
ENV DB_USER="appuser"
ENV DB_PASS="AppPass123!"
ENV SERVER_PORT="8084"
ENV SPRING_CONFIG_LOCATION=./application.properties

EXPOSE 8084

# Healthcheck básico (requiere Actuator habilitado)
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8084/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]