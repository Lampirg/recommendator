FROM openjdk:19-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/mal-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-jar", "/app/mal-0.0.1-SNAPSHOT.jar"]