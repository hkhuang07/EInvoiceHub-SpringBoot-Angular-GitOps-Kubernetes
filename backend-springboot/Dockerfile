
# Stage 1: Builder Stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /build

COPY pom.xml .

RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine AS runtime
LABEL maintainer="EInvoiceHub Team" \
      project="EInvoiceHub"
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -Djdk.tracePinnedThreads=short"

RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -s /bin/sh -D appuser

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar
RUN mkdir -p /app/logs && chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
CMD ["--spring.profiles.active=prod"]


FROM builder AS dev
WORKDIR /build
EXPOSE 5005

ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
ENTRYPOINT ["mvn", "spring-boot:run", "-Dspring-boot.run.profiles=dev"]