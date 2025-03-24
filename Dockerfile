# Build stage
FROM maven:3.8-openjdk-8-slim AS build
WORKDIR /app

# Copy pom.xml first to leverage Docker cache for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build the application
COPY src ./src
RUN mvn package -DskipTests

# Runtime stage
FROM openjdk:8-jre-slim
WORKDIR /app

# Create upload directory
RUN mkdir -p /app/src/main/resources/static/upload

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set user to run the application (security best practice)
RUN useradd -m javauser
USER javauser

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"] 