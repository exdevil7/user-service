# Stage 1: Build the application
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

# Copy gradle executable and configuration
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Grant execution rights and download dependencies (cached layer)
RUN ./gradlew --no-daemon dependencies

# Copy source code and build
COPY src src
RUN ./gradlew --no-daemon clean bootJar

# Stage 2: Create the runtime image
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
