FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY target/rag-service.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]