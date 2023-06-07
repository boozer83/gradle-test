package io.kakaoi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.nio.file.Path;

/**
 * 서비스 Properties
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private Proxy proxy;

    private String outboundUrl;

    private Path tempPath;

    private String errorPage;

    private Endpoint kicWebConsole;

    private Endpoint iam;

    private Account systemAccount;

    private Metering metering;

    private CronJob cronJob;

    private Trail trail;

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public String getOutboundUrl() {
        return outboundUrl;
    }

    public void setOutboundUrl(String outboundUrl) {
        this.outboundUrl = outboundUrl;
    }

    public Path getTempPath() {
        return tempPath;
    }

    public void setTempPath(Path tempPath) {
        this.tempPath = tempPath;
    }

    public String getErrorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public static class Proxy {
        private boolean enabled;
        private URI http;
        private URI https;
        private String noProxy;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public URI getHttp() {
            return http;
        }

        public void setHttp(URI http) {
            this.http = http;
        }

        public URI getHttps() {
            return https;
        }

        public void setHttps(URI https) {
            this.https = https;
        }

        public String getNoProxy() {
            return noProxy;
        }

        public void setNoProxy(String noProxy) {
            this.noProxy = noProxy;
        }
    }

    public static class Endpoint {
        private URI endpoint;

        public URI getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(URI endpoint) {
            this.endpoint = endpoint;
        }
    }

    public static class Account {

        private String domainName;

        private String projectName;

        private String username;

        private String password;

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class Metering {

        private String serviceId;

        private Integer productId;

        private String regionCode;

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getRegionCode() {
            return regionCode;
        }

        public void setRegionCode(String regionCode) {
            this.regionCode = regionCode;
        }
    }

    public static class CronJob {

        private String metering;

        public String getMetering() {
            return metering;
        }

        public void setMetering(String metering) {
            this.metering = metering;
        }

    }

    public static class Trail {

        private String topic;

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }

    public Endpoint getKicWebConsole() {
        return kicWebConsole;
    }

    public void setKicWebConsole(Endpoint kicWebConsole) {
        this.kicWebConsole = kicWebConsole;
    }

    public Endpoint getIam() {
        return iam;
    }

    public void setIam(Endpoint iam) {
        this.iam = iam;
    }

    public Account getSystemAccount() {
        return systemAccount;
    }

    public void setSystemAccount(Account systemAccount) {
        this.systemAccount = systemAccount;
    }

    public Metering getMetering() {
        return metering;
    }

    public void setMetering(Metering metering) {
        this.metering = metering;
    }

    public CronJob getCronJob() {
        return cronJob;
    }

    public void setCronJob(CronJob cronJob) {
        this.cronJob = cronJob;
    }

    public Trail getTrail() {
        return trail;
    }

    public void setTrail(Trail trail) {
        this.trail = trail;
    }
}
