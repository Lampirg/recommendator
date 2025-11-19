FROM eclipse-temurin:17
ARG JAR_FILE=target/*.jar
COPY ./target/recommendator-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-jar", "/app/recommendator-0.0.1-SNAPSHOT.jar"]