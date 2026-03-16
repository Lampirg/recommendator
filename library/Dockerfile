FROM eclipse-temurin:17-alpine AS build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:resolve

COPY src src

RUN ./mvnw install -DskipTests