FROM openjdk:25-ea-23-jdk-slim

WORKDIR /app

COPY target/wander-v1T.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]