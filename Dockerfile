FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./streaming_serviceREST/target/streaming_serviceREST-1.0-SNAPSHOT.jar /app

EXPOSE 8083

CMD ["java", "-jar", "streaming_serviceREST-1.0-SNAPSHOT.jar"]