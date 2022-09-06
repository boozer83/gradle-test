# kic-spring-template

## 템플릿 구조 및 개발 방법

* 하위 문서인 [dev-docs/README.md](dev-docs/README.md) 참조

## Docker Build

```shell
docker build --no-cache --pull -t idock.daumkakao.io/cloud-service/sample:latest .
```

## Local Development Run

```shell
cd local-dev
docker compose up -d
cd ..
./gradlew clean bootRun --args='--spring.profiles.active=local'
```

## Application Properties

* `src/main/resources/config/` 폴더 내의 프로파일 별 Yaml 파일 위치.
  * `application.yml`: 기본 설정
  * `application-local.yml`: 로컬 개발 환경
  * `application-dev.yml`: 개발 환경
  * `application-stg.yml`: stage (sandbox) 환경
  * `application-prod.yml`: production 환경

* 기타 mocking profile
  * mock-iam: IAM 서버 인증을 거치지 않고 테스트 용도 목업을 적용
  * mock-metering: 스케줄러 작동 여부 확인용

### App Env Variables

| Prefix                          | Key               | Description                                                   |
|---------------------------------|-------------------|---------------------------------------------------------------|
| application.proxy               | enabled           | 프록시 설정 활성 플래그                                                 |
| application.proxy               | http              | HTTP Proxy URL                                                |
| application.proxy               | https             | HTTPS Proxy URL                                               |
| application.proxy               | no-proxy          | 프록시 적용 예외(NO_PROXY 환경변수)                                      |
| application                     | outbound-url      | API 외부 접근 URL                                                 |
| application                     | temp-path         | Temp Dir                                                      |
| application.kic-web-console     | endpoint          | KIC Console UI 접근 Endpoint URL                                |
| application.iam                 | endpoint          | KIC IAM 접근 Endpoint URL                                       |
| application.system-account      | domain-name       | KIC 시스템 연동 시 사용할 계정 소속 도메인 이름                                 |
| application.system-account      | project-name      | KIC 시스템 연동 시 사용할 계정 소속 프로젝트 이름                                |
| application.system-account      | username          | KIC 시스템 연동 시 사용할 계정 유저 이름                                     |
| application.system-account      | password          | KIC 시스템 연동 시 사용할 계정 패스워드                                      |
| application.metering            | service-id        | KIC Metering Service ID                                       |
| application.metering            | product-id        | KIC Metering Product ID                                       |
| application.metering            | region-code       | KIC Metering Region Code                                      |
| application.cron-job            | metering          | 미터링 스케쥴러 동작 간격                                                |
| spring.kafka                    | bootstrap-servers | 미터링 카프카 서버                                                    |
| spring.kafka.template           | default-topic     | 미터링 토픽 이름                                                     | 
