FROM maven:3.8.1-adoptopenjdk-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -q -f /usr/src/app/pom.xml clean package -Dmaven.test.skip=true
RUN mkdir -p /usr/src/app/target/dependency && (cd /usr/src/app/target/dependency; jar -xf ../*.jar)

# FROM openjdk:7u211-jdk-alpine
FROM adoptopenjdk/openjdk11:latest
ARG DEPENDENCY=/usr/src/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENV GOOGLE_CLIENT_CREDENTIALS=
ENV MYSQL_DATABASE_PASSWORD=

EXPOSE 9090
ENTRYPOINT ["java","-cp","app:app/lib/*", "-Ddatabase.password=${MYSQL_DATABASE_PASSWORD}", "-Dgoogle.client.credentials=${GOOGLE_CLIENT_CREDENTIALS}", "com.mailchatview.backend.MailChatView"]