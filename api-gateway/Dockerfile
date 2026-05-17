# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

RUN --mount=type=cache,target=/root/.m2 \
    echo "Preparing Maven cache"

# Copy Maven files
COPY pom.xml .
COPY .mvn .mvn

# Copy source code
COPY src ./src

# Build application
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Install curl for healthcheck
RUN apk add --no-cache curl

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copy JAR from build stage and set ownership in one layer
COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

# Switch to non-root user
USER spring:spring

# Expose Gateway port
EXPOSE 8080

# Start period más largo porque Gateway valida JWT y conecta con Eureka
HEALTHCHECK --interval=30s --timeout=5s --start-period=90s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM options optimized for API Gateway
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+UseContainerSupport -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]