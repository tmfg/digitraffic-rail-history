FROM openjdk:17-jre-slim
VOLUME /tmp
ADD target/train-history-updater-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]