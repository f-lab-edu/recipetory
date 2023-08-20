FROM openjdk:17-jdk-alpine
ENV	USE_PROFILE=local
VOLUME /tmp
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java",\
"-Dspring.profiles.active=${USE_PROFILE}",\
"-jar",\
"/app.jar"]