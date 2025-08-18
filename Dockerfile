
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos primero solo el pom.xml para aprovechar el cache de Docker
COPY pom.xml .

# Descargamos las dependencias (esto se cachea si el pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Compilamos el proyecto
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
