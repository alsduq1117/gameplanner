FROM openjdk:11-oracle

VOLUME /tmp

ARG JAR_FILE=/build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["nohup", "java", "-jar", "app.jar", "&"]