FROM eclipse-temurin:17 AS build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY --from=animerecommendator-library:latest /root/.m2 /root/.m2

RUN ./mvnw -B -e org.apache.maven.plugins:maven-dependency-plugin:go-offline -DexcludeArtifactIds=library

COPY src src

RUN ./mvnw install -DskipTests

FROM eclipse-temurin:17
ARG DEPENDENCY=/workspace/app/target
COPY --from=build ${DEPENDENCY}/recommendator.jar /app/recommendator.jar
ENTRYPOINT ["java","-jar","/app/recommendator.jar"]