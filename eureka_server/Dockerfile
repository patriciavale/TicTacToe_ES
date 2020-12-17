FROM openjdk:11-jdk-oracle
VOLUME /tmp
ARG JAR_FILE=/target/eureka_server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8250