FROM cloudpipeline.kr-central-1.kcr.dev/tekton-task/gradle:6.4.1-jdk8 AS build

WORKDIR /app

# add project
ADD . /app/

# server mvn install
RUN gradle clean bootJar --exclude-task asciidoctor --exclude-task test

###

FROM docker.io/eclipse-temurin:8-jre-focal AS app

# example: os update / install skopeo, kubectl / remove apt update cache
#RUN sed -i -re "s/([a-z]{2}.)?archive.ubuntu.com|security.ubuntu.com/mirror.kakao.com/g" /etc/apt/sources.list; \
#    apt-get update && apt-get install --no-install-recommends -y apt-transport-https ca-certificates curl gnupg; \
#    curl -fsSLo /usr/share/keyrings/kubernetes-archive-keyring.gpg https://packages.cloud.google.com/apt/doc/apt-key.gpg; \
#    curl -fsSL https://download.opensuse.org/repositories/devel:kubic:libcontainers:stable/xUbuntu_20.04/Release.key | gpg --dearmor | tee /etc/apt/trusted.gpg.d/devel_kubic_libcontainers_stable.gpg > /dev/null; \
#    echo 'deb http://download.opensuse.org/repositories/devel:/kubic:/libcontainers:/stable/xUbuntu_20.04/ /' | tee /etc/apt/sources.list.d/devel:kubic:libcontainers:stable.list > /dev/null; \
#    echo "deb [signed-by=/usr/share/keyrings/kubernetes-archive-keyring.gpg] https://apt.kubernetes.io/ kubernetes-xenial main" | tee /etc/apt/sources.list.d/kubernetes.list > /dev/null; \
#    apt-get update && apt-get upgrade --no-install-recommends -y &&  \
#    apt-get install --no-install-recommends -y kubectl skopeo; \
#    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /app/build/libs/server.jar .

EXPOSE 8080

# server run
ENTRYPOINT ["java"]
CMD ["-jar", "/app/server.jar"]
