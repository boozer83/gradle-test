FROM docker.io/gradle:6.9.1-jdk8 AS build

WORKDIR /app

# add project
ADD . /app/

# server mvn install
RUN gradle clean bootJar --exclude-task asciidoctor --exclude-task test

###

FROM --platform=linux/amd64 docker.io/eclipse-temurin:8-jre-focal AS app

WORKDIR /app
COPY --from=build /app/build/libs/server.jar .

EXPOSE 8080

# server run
ENTRYPOINT ["java"]
CMD ["-jar", "/app/server.jar"]
