## 템플릿 구조 및 개발 방법

### 폴더 구조 및 간략 요약

```
kic-spring-template
|- chart ----------------------------------------- # chart example (README.md 포함)
|- local-dev ------------------------------------- # 로컬 환경
  |- .volumes ------------------------------------ # Local DB Volume 연결 폴더
  |- config/db/init.sql -------------------------- # Local DB 초기화 SQL
  |- docker-compose.yml -------------------------- # Local DB 실행 Docker Compose
|- src
  |- docs/asciidoc ------------------------------- # spring rest docs asciidoc
  |- main
    |- java
      |- io.kakaoi
        |- aop
        |- config
          |- ApplicationProperties.java ---------- # Config 값이 저장되어 있는 클래스
          |- CloudIAMAuthFilter.java ------------- # Cloud IAM 인증 시 사용하는 Spring Security Filter
          |- CloudIAMAuthProvider.java ----------- # Cloud IAM 인증을 담당하는 AuthenticationProvider 구현 클래스
          |- CloudIAMAuthToken.java -------------- # Cloud IAM 인증 데이터를 담는 Authentication 구현 클래스
          |- Constants --------------------------- # 상수 값이 담긴 클래스
          |- ResourcePermissionEvaluator.java ---- # hasPermission Custom Class
          |- OffsetPageRequest.java -------------- # Pageable 구현된 offset 형태의 PageRequest 클래스
          |- OffsetPageRequestResolver.java ------ # Spring Data 내 Pageable 변환을 OffsetPageRequest로 바꾸기 위한 Resolver
          |- PageableConfiguration.java ---------- # Pageable 설정 클래스
          |- SpringDocConfiguration.java --------- # SpringDoc (Swagger) 설정 클래스
          |- WebSecurityConfig.java -------------- # Spring Security Config
        |- domain -------------------------------- # Entity Classes Package
        |- exception
          |- BusinessException.java -------------- # 기본 처리 예외 클래스. (Unchecked Exception)
          |- GlobalExceptionHandler.java --------- # 예외 시 응답 처리 핸들러
        |- repository ---------------------------- # Entity (DB) 접근 Package
        |- scheduler
          |- ScheduleManager.java ---------------- # 스케쥴러
        |- security ------------------------------ # 인증, 인가 관련 Package
        |- service ------------------------------- # Business Logic 담긴 Service Class Package
          |- dto
            |- IAMDTO.java ----------------------- # Cloud IAM 구조 DTO 묶음 클래스
            |- MeteringDTO.java ------------------ # Metering 구조 DTO 묶음 클래스
            |- SampleDTO.java -------------------- # Sample DTO 묶음 클래스
          |- iam
            |- CloudIAMMockService.java ---------- # mock-iam 활성화 시 구현체 서비스 대신 동작하는 서비스 
            |- CloudIAMProductionService.java ---- # Cloud IAM 연동 서비스 클래스
            |- CloudIAMService.java -------------- # Cloud IAM 연동 서비스 클래스 인터페이스
            |- CloudIAMTokenMockService.java ----- # mock-iam 활성화 시 구현체 서비스 대신 동작하는 서비스
            |- CloudIAMTokenProductionService.java # Cloud IAM 토큰 발급 서비스
            |- CloudIAMTokenService.java --------- # Cloud IAM 토큰 발급 서비스 인터페이스
          |- metering
            |- CloudMeteringService.java --------- # 실제 일부 구현되어 있는 미터링 서비스 (커스텀 필요)
            |- LogPrintMeteringService.java ------ # mock-metering 시에 로그만 출력하는 미터링 서비스
            |- MeteringService.java -------------- # 미터링 서비스 인터페이스
          |- predicate --------------------------- # KiC 리소스 검색 조건 JPA Specification Factory 클래스 패키지
            |- AbstractFilterPredicateFactory.java # 검색 조건 구현 추상 팩토리 클래스
            |- SampleFilterPredicateFactory.java - # SampleEntity 검색 조건 구현 팩토리 클래스
          |- ProjectService.java ----------------- # IAM Project 서비스
          |- ResourcePermissionService.java ------ # 권한 인가 관련 서비스
          |- SampleService.java ------------------ # 예제 서비스
        |- util ---------------------------------- # Util Package 
          |- SecurityUtils.java ------------------ # 인증 데이터를 가져오는 유틸 
        |- web.rest ------------------------------ # Controller Package
          |- vm ---------------------------------- # Controller Parameter를 받는 VO Package
            |- vaild  ---------------------------- # Validator 작성 Sample
            |- FilterVM.java --------------------- # KiC 검색 필터 매개변수
            |- SampleVM.java --------------------- # 샘플 컨트롤러 매개변수
          |- Result.java ------------------------- # 리턴 Wrapper 객체
          |- SampleController.java --------------- # 예제용 Sample Controller
          |- HealthController.java --------------- # Helthcheck 용 컨트롤러
        |- Application.java ---------------------- # 프로그램 시작 위치
    |- resources
      |- config ---------------------------------- # Application Properties 항목 참조
      |- data ------------------------------------ # iam mocking data 존재
      |- i18n ------------------------------------ # MessageSource로 다국어 참조 시 사용
  |- test   -------------------------------------- # 테스트 코드 (testcontainers, mockmvc, spring rest docs 예제 포함)
|- build.gradle ---------------------------------- # gradle buildscript 예제
|- Dockerfile ------------------------------------ # Docker build 예제
|- README.md ------------------------------------- # 현재 이 파일!
```

### local DB 구동 방법

1. Docker Desktop 설치 [링크](https://www.docker.com/products/docker-desktop/)
   * KEP 내 사용 시 라이센스 발급 후 사용
2. 터미널로 local-dev 폴더 들어간 후 아래 명령어 입력
    ```sh
    docker-compose up -d
    ```
   
* 만약 다음과 같은 Error 발생 시 3306 포트가 이미 사용중이므로 프로세스를 죽이거나, 포트를 바꿔야함.
    ```
    Error response from daemon: driver failed programming external connectivity on endpoint local-dev-mysql-1 (1d68298b3deb98fbd1bdeaaa415f0c774d68aa3395bc48a411fa44c61c7d0231): Bind for 0.0.0.0:3306 failed: port is already allocated
    ```
    * `local-dev/docker-compose.yml` 파일을 열어 mysql 하위 `ports`를 다음과 같이 수정
    ```yaml
        ports:
          - "3307:3306"
    ```
### Controller 응답 시 Result Wrapper 클래스 사용 방법

결과를 던질 때 결과 데이터는 Result 객체에 Wrapping 해 응답한다.

#### Body 형태
```json
{
    "code": "0200",
    "message": "OK",
    "status": 200,
    "data": {
        "innerField": "innerData"
    }
}
```

* "data": 값은 요청 형태에 따라 다르며, null이 올 수 있다.
* "message": 호출 성공 시엔 "OK" 메시지이며, 에러 시 에러 메시지가 담긴다.
* "status": HTTP Status 코드를 정수 형태로 반환한다.
* "code": 내부 상태 상수 코드이다.

#### 코드 작성 및 공통 코드

* `Result` 객체는 static 생성 메소드를 제공한다.
  * `Result.ok(object)`, `Result.error()`와 같은 메소드로 생성
  * `Result.of()` 메소드로 상태 커스텀이 가능
  * Paging 처리 시엔 `Result.okWithPaging()` 메소드 사용 권장
    * 기본 `Page`에서 offset paging 처리 응답을 위한 `PagingDTO`로 변환되어 응답
* Error 처리 시는 서비스 내에서 `BusinessException` 예외 클래스를 활용하는 것을 권장.
  * exception 내 `GlobalExceptionHandler` 가 `BusinessException` 내용을 읽어 알맞은 형태로 Result 형태로 응답.
* 공통 코드는 config 패키지 내 Constants 클래스 하위 CommonCode enum으로 작성되어 있음.
  * 커스텀 코드 작성 시에 400 에러 위치는 code, message 내용이 응답되지만, 500 에러 위치는 code만 전달되고 메시지는 "Server Error"로 통일되게 나간다.
    * 예기치 못한 에러 응답 시 혹시 모를 보안 위협으로 메시지 내용 통일

### Offset Paging

* Controller 내에서 `org.springframework.data.domain.Pageable` 파라메터 등록 시 자동으로 `OffsetPageRequest` 형태로 받아오게 설정.
* 따라서 그대로 Pageable 형태로 JPA 요청을 받으면 됨.
* 응답 시엔 서비스에선 `org.springframework.data.domain.Page` 인터페이스를 그대로 사용 후에 컨트롤러 응답 시 `Result.okWithPaging()` 메소드를 이용해 응답.

### Cloud IAM 인증 매커니즘

* 인증 요청 Flow
    ```
    요청 ---> CloudIAMAuthFilter 접근
        ---> CloudIAMAuthToken 생성 후 CloudIAMAuthProvider 전달
        ---> CloudIAMAuthProvider 내에서 CloudIAMService.getUserInfo() 요청
        ---> 응답 내용을 CloudIAMAuthToken에 담고 리턴
        ---> CloudIAMAuthFilter 에서 SecurityContextHolder 에 인증 내용 삽입
        ---> 컨트롤러 접근
    ```
* CloudIAMAuthToken.mappingAuthorities() 에서 내부 role mapping 가능

#### 시퀀스 다이어그램 ([Container Registry](https://github.kakaoenterprise.in/cloud-service-provider/kic-container-registry) 서비스 기준)

![auth-diagram](assets/01-iam-auth-sequence-diagram.svg)

### 권한 정의 및 설정

* 권한 정의는 별도로 커스텀.
* ResourcePermissionEvaluator 클래스 통하여 hasPermission 커스텀 가능
* ResourcePermissionService 클래스를 최소 구현하여 예제 제공

### Swagger 구성 및 접근 방법

* openapi 3.0 적용으로 springfox swagger 아닌 springdoc swagger 사용
* `/swagger-ui` 경로 요청 시 Swagger UI 접근 가능
* @Operation description 항목 사용 시 markdown 사용 가능 및 줄바꿈 시 '\n' 이스케이프 문자 사용.

### HTTP 호출 시 RestTemplate 사용 (proxy / no-proxy)

* RestTemplate 사용 시 객체 생성을 직접하는 것은 제대로 된 동작을 보증하지 않음. (프록시 세팅 등)
* OkHttp, logging-interceptor 라이브러리 적용으로 요청/응답 로그 표시
* 아래와 같은 방법으로 사용 가능
    * 내부 망 사용 시
      ```java
      @Autowired
      private RestTemplate restTemplate;
      ```
    * 프록시를 통과하여 외부 망 사용 시 (properties 내 no-proxy 설정 시 내부 망 사용)
      ```java
      @Autowired
      @Qualifier("proxyRestTemplate")
      private RestTemplate proxyRestTemplate;
      ```
* proxy 설정은 resource/config 내 ApplicationProperties 에서 설정 가능

### metering 사용

* metering 형태는 프로젝트 마다 다를 수 있므로 기획 문의
* product-id 값은 유효한 정수를 넣어야 함
* 기본적인 spring kafka client 설정 완료
* 자세한 내용은 [미터링 가이드](https://wiki.daumkakao.com/pages/viewpage.action?pageId=849056830) 참조

### KiC 검색 필터

* 현재 KiC 서비스 내 검색 필터는 다음과 같은 구조를 가짐.
    * 필터: <컬럼: 검색어> 형태를 가짐
    * 키워드: 검색어만 존재
    * 위 항목들이 칩형태로 쌓인다.
* Filter Parameter를 받을 수 있는 web.rest.vm 패키지 내 `FilterVM` 존재
    * `@ModelAttribute` 애노테이션과 함께 컨트롤러 파라미터 내 삽입
* 필터, 키워드 검색 조건은 다음과 같다.
    * 필터간은 and 조건으로 묶임
    * 키워드간은 or 조건으로 묶임
    * 필터와 키워드 사이는 and 조건으로 묶임
* service.predicate 패키지 내 `AbstractFilterPredicateFactory` 클래스를 통해 손쉽게 구현가능.
  * JPA Specification 기능을 사용해 동적 쿼리 구현
  * hibernate-jpamodelgen 라이브러리 통해 엔티티 구현 시에 metamodel 클래스 (Entity_) 자동생성
  * `AbstractFilterPredicateFactory`를 상속해 필터 조건와 키워드 컬럼을 선택하는 메소드를 구현해 make() 메소드로 생성.
  * 예제인 `SampleFilterPredicateFactory` 참조

