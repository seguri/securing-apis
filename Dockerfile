FROM openjdk:13-jdk-alpine3.10

VOLUME /tmp

RUN mkdir -p /app/
RUN mkdir -p /app/images/

# adds jar app
ADD build/libs/ms1-1.0-SNAPSHOT.jar /app/app.jar

# adds configuration files
ADD src/main/resources/application.properties /app/application.properties
ADD src/main/resources/db/hikari.properties /app/hikari.properties
ADD src/main/resources/xsd/batchusers.xsd /app/batchusers.xsd

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait /wait
RUN chmod +x /wait

EXPOSE 8080