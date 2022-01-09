FROM adoptopenjdk/openjdk14:latest
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} reactive=docker-service.jar
ENTRYPOINT ["java","-jar","/reactive=docker-service.jar"]
EXPOSE 8761
MAINTAINER bharat.savanur@gmail.com