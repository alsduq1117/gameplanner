FROM openjdk:17-oracle

VOLUME /tmp

ARG JAR_FILE=/build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "-DRDS_HOSTNAME=${RDS_HOSTNAME}", "-DRDS_PORT=${RDS_PORT}", "-DRDS_DB_NAME=${RDS_DB_NAME}", "-DRDS_USERNAME=${RDS_USERNAME}", "-DRDS_PASSWORD=${RDS_PASSWORD}"]
