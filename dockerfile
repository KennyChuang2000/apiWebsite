FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY springboot-app/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]