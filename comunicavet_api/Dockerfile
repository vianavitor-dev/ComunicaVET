# Etapa 1: build usando Java 25
FROM eclipse-temurin:25-jdk-noble AS build

RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY . .
RUN mvn clean package -DskipTests


# Etapa 2: runtime
FROM amazoncorretto:25
WORKDIR /app
COPY --from=build /app/target/comunicavet-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/comunicavet-0.0.1-SNAPSHOT.jar"]
