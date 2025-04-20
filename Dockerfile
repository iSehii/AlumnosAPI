# Etapa de construcción
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Etapa final
FROM eclipse-temurin:17-jre
WORKDIR /app
EXPOSE 8080
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
