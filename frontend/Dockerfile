FROM openjdk:19-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/frontend-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-jar", "/app/frontend-0.0.1-SNAPSHOT.jar"]