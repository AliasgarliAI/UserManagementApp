FROM alpine:latest
RUN apk add --no-cache openjdk11
COPY build/libs/demo-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app/
ENTRYPOINT ["java"]
CMD ["-jar", "/app/demo-0.0.1-SNAPSHOT.jar"]
