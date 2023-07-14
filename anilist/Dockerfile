FROM openjdk:19-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/anilist-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-jar", "/app/anilist-0.0.1-SNAPSHOT.jar"]