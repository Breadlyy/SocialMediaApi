FROM openjdk:19
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY target/SocialMediaApi-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]