FROM eclipse-temurin:21-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY prod.env prod.env

ENV DB_DDL_AUTO=create

ENV KEYCLOAK_SERVER_URL=https://keycloak6.sparta-project.xyz
ENV KEYCLOAK_REALM=delivery
ENV KEYCLOAK_CLIENT_ID=spring-app

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 3000