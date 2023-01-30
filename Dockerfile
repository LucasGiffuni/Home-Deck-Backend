FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} Home-Deck-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Home-Deck-0.0.1.jar"]
