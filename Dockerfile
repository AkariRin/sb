# Multi-stage Dockerfile for Spring Boot Application
# Stage 1: Build stage
FROM eclipse-temurin:25-jdk-jammy AS builder

# Set working directory
WORKDIR /app

# Copy gradle wrapper files first (for better caching)
COPY gradlew .
COPY gradlew.bat .
COPY gradle gradle/

# Fix permissions for gradlew (important for Linux-based Docker builds)
RUN chmod +x gradlew

# Copy build configuration files
COPY build.gradle .
COPY settings.gradle .

# Download dependencies (cached layer if dependencies don't change)
RUN ./gradlew dependencies --no-daemon || return 0

# Copy source code
COPY src src/

# Build the application (skip tests for faster builds)
RUN ./gradlew bootJar --no-daemon -x test

# Stage 2: Runtime stage
FROM eclipse-temurin:25-jre-jammy

# Create a non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring

# Set working directory
WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Change ownership to non-root user
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring:spring

# Expose the application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set JVM options for containerized environments
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

