# Multi-stage build
FROM maven:3.8.1-jdk-11 as build
WORKDIR /workspace/app

# Copy required pom & source
COPY pom.xml .
COPY src src

# package the project using a cache for the maven dependencies
RUN --mount=type=cache,target=/root/.m2 mvn clean package -Dmaven.test.skip

# Unpack the fat jar so that we may have it layered
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:17-alpine

# Set groups & users for more security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG DEPENDENCY=/workspace/app/target/dependency

# Copy layers
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Start the application from the main class
ENTRYPOINT ["java","-Dspring.profiles.active=compose","-cp","app:app/lib/*","finki.ukim.kgt.kgtontology.KgtOntologyApplicationKt"]