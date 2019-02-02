FROM openjdk:8-jdk-alpine
RUN mkdir -p /logs
ARG JAR_FILE=target/crawler-*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "./app.jar"]