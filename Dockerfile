FROM gradle:8.7.0-jdk21-alpine AS build
WORKDIR /app
COPY gradle /app/gradle
COPY build.gradle settings.gradle /app/
RUN gradle dependencies -i --stacktrace
COPY src /app/src
RUN gradle build -i --stacktrace


FROM openjdk:21-slim-bookworm
WORKDIR /app
#ENV FIREBASE_KEY_PATH=/app/
COPY eshopeule-firebase-key.json /app/
COPY --from=build /app/build/libs/*.jar /app/spring-boot-application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]