FROM gradle:6.9.1-jdk8 AS build

WORKDIR /app

# add project
ADD . /app/

# server mvn install
RUN gradle -Dhttp.proxyHost=sec-proxy.k9e.io -Dhttp.proxyPort=3128 -Dhttps.proxyHost=sec-proxy.k9e.io -Dhttps.proxyPort=3128 -Dhttp.nonProxyHosts=localhost,127.0.0.1,127.0.0.0/8,192.168.0.0/16,10.0.0.0/8,172.16.0.0/12,.k9e.io,.kakaoi.com,.kakaoicdn.net,.k9etool.io,.k5d.io,.kakaoenterprise.com,.kakaoicloud.com,.kakaoi.io,.kakaoi.ai,.kakaoicloud.in,github.kakaoenterprise.in,mdock.daumkakao.io,idock.daumkakao.io clean bootJar --exclude-task asciidoctor --exclude-task test

###

FROM --platform=linux/amd64 eclipse-temurin:8-jre-focal AS app

WORKDIR /app
COPY --from=build /app/build/libs/server.jar .

EXPOSE 8080

# server run
ENTRYPOINT ["java"]
CMD ["-jar", "/app/server.jar"]
