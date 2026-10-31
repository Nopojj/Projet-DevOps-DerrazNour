# Etape 1 : build et tests avec l'image officielle Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package

# Etape 2 : image d'execution legere (JRE uniquement)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/pipeline-demo-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]