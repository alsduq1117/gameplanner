FROM openjdk:17-oracle

VOLUME /tmp

ARG JAR_FILE=/build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
