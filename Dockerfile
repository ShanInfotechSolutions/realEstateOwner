FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/SpringBootMicroservicesOwnerClient-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9191
ENTRYPOINT ["java", "-jar", "app.jar"]