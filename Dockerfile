FROM adoptopenjdk:11-jdk-hotspot
COPY build/libs/demo-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app/
ENTRYPOINT ["java"]
CMD ["-jar", "/app/demo-0.0.1-SNAPSHOT.jar"]
