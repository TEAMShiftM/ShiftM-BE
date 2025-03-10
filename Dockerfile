FROM openjdk:21-jdk-slim
COPY build/libs/shiftm-0.0.1-SNAPSHOT.jar shiftm.jar
ENTRYPOINT ["java", "-jar", "shiftm.jar"]
