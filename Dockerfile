# Build stage with Maven + JDK 21
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven config first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the Spring Boot jar
RUN mvn clean package -DskipTests

# Runtime stage with only JDK 21
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy built jar from previous stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
