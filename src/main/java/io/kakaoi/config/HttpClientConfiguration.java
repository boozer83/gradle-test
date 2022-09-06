package io.kakaoi.config;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * HTTP 상의 Rest API를 호출하기 위해 RestTemplate 객체를 설정하고 Bean 등록하는 클래스.
 */
@Configuration
public class HttpClientConfiguration {

    private static final Logger log = LoggerFactory.getLogger(HttpClientConfiguration.class);

    @Autowired
    private Environment env;

    @Autowired
    private BuildProperties buildProperties;

    /**
     * OkHttpClient 객체를 생성한다.
     * @return OkHttpClient Object
     */
    @Bean @Primary
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(30))
            .readTimeout(Duration.ofMinutes(10))
            .writeTimeout(Duration.ofMinutes(10))
            .addInterceptor(okHttpUserAgentInterceptor());

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (env.acceptsProfiles(Constants.NOT_EXACT_PROD_PROFILES)) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        clientBuilder.addNetworkInterceptor(loggingInterceptor);

        return clientBuilder.build();
    }

    /**
     * 프록시 설정된 OkHttpClient 객체를 생성한다.
     * @return proxyOkHttpClient
     */
    @Bean("proxyOkHttpClient")
    public OkHttpClient proxyOkHttpClient(ApplicationProperties applicationProperties) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if (applicationProperties.getProxy().isEnabled()) {
            clientBuilder.proxySelector(new HttpClientProxySelector(applicationProperties));
        }

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (env.acceptsProfiles(Constants.NOT_EXACT_PROD_PROFILES)) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        clientBuilder.addNetworkInterceptor(loggingInterceptor);

        return clientBuilder.build();
    }

    /**
     * RestTemplateBuilder 를 통해 OkHttpClient를 적용한 ClientHttpRequestFactory를 등록하고
     * Interceptor를 설정한 RestTemplate를 반환한다.
     * @return 서비스에서 호출 가능한 RestTemplate 객체
     */
    @Bean @Primary
    public RestTemplate restTemplate(OkHttpClient okHttpClient, RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
            .requestFactory(() -> new OkHttp3ClientHttpRequestFactory(okHttpClient))
            .build();
    }

    /**
     * RestTemplateBuilder 를 통해 proxyOkHttpClient를 적용한 ClientHttpRequestFactory를 등록하고
     * Interceptor를 설정한 RestTemplate를 반환한다.
     * @return 서비스에서 호출 가능한 proxyRestTemplate 객체
     */
    @Bean("proxyRestTemplate")
    public RestTemplate proxyRestTemplate(
        @Qualifier("proxyOkHttpClient") OkHttpClient proxyOkHttpClient,
        RestTemplateBuilder restTemplateBuilder
    ) {
        return restTemplateBuilder
            .requestFactory(() -> new OkHttp3ClientHttpRequestFactory(proxyOkHttpClient))
            .build();
    }

    /**
     * User-Agent Header를 삽입한다.
     * @return OkHttp User Agent Interceptor
     */
    private Interceptor okHttpUserAgentInterceptor() {
        return chain -> {
            Request originRequest = chain.request();

            Request request = originRequest.newBuilder()
                .header(HttpHeaders.USER_AGENT, "KIC-CR-API-Server/" + buildProperties.getVersion())
                .build();

            return chain.proceed(request);
        };
    }

}
