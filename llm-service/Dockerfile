# -------------------------------
# Stage 1: Build
# -------------------------------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# -------------------------------
# Stage 2: Runtime
# -------------------------------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/target/llm-service-1.0.0.jar app.jar

# Security: run as non-root
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
USER appuser

# Expose port
EXPOSE 8081

# JVM optimizations
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

# Start application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]