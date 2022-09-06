package io.kakaoi.config;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HttpClientProxySelector extends ProxySelector {

    private final List<Proxy> noProxies;

    private final List<Proxy> proxies;

    private final List<NoProxyHost> noProxyHosts;

    public HttpClientProxySelector(ApplicationProperties applicationProperties) {
        this.noProxyHosts =
            Arrays.stream(applicationProperties.getProxy().getNoProxy().split(","))
                .map(NoProxyHost::new)
                .collect(Collectors.toList());

        URI proxyUri = applicationProperties.getProxy().getHttp();

        this.noProxies = Collections.singletonList(Proxy.NO_PROXY);
        this.proxies = Collections.singletonList(
            new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUri.getHost(), proxyUri.getPort())));
    }

    @Override
    public List<Proxy> select(URI uri) {
        for (NoProxyHost noProxyHost : noProxyHosts) {
            if (noProxyHost.matches(uri.getHost(), uri.getPort())) {
                return noProxies;
            }
        }
        return proxies;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {}

    private static class NoProxyHost {

        private static final int NO_PORT = -1;

        private final Pattern hostPattern;

        private final int port;

        public NoProxyHost(String hostPattern) {
            String[] hostPortSplit = hostPattern.split(":");
            if (hostPortSplit.length == 2) {
                this.port = Integer.parseInt(hostPortSplit[1]);
            }
            else {
                this.port = NO_PORT;
            }
            this.hostPattern = makeHostPattern(hostPortSplit[0]);
        }

        public boolean matches(String host) {
            return this.matches(host, NO_PORT);
        }

        public boolean matches(String host, int port) {
            return hostPattern.matcher(host).matches()
                && (this.port == NO_PORT || this.port == port);
        }

        private static Pattern makeHostPattern(String hostPatternStr) {
            String pattern = hostPatternStr.trim()
                .replaceAll("\\*", "[\\w\\\\.-]*")
                .replaceAll("\\.", "\\\\.");

            if (pattern.startsWith("\\.")) {
                pattern = "[\\w\\\\.-]*" + pattern;
            }

            return Pattern.compile(pattern);
        }
    }
}
