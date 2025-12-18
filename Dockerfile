FROM eclipse-temurin:25-jdk-jammy AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle/

RUN chmod +x gradlew

COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies --no-daemon || return 0

COPY src src/

RUN ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:25-jre-jammy

RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN chown -R spring:spring /app

USER spring:spring

EXPOSE 8000

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
