FROM openjdk:11-jdk-oracle
VOLUME /tmp
ARG JAR_FILE=/target/game_manager-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8253