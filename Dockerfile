# For Java 11, try this
# FROM adoptopenjdk/openjdk11:alpine-jre
FROM openjdk:8-jdk-alpine
# Refer to Maven build -> finalName
ARG JAR_FILE=target/apirest.jar
# cd /opt/apirest
WORKDIR /opt/apirest
# cp target/apirest.jar /opt/apirest/apirest.jar
COPY ${JAR_FILE} apirest.jar
# java -jar /opt/apirest/apirest.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar","apirest.jar"]