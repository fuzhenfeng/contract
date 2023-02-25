package org.contract.remote;

import javassist.util.proxy.MethodHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.Http;
import org.contract.common.JsonUtils;
import org.contract.common.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class RemoteMethodHandler implements MethodHandler {
    private final static Logger log = LogManager.getLogger(RemoteMethodHandler.class);

    private Class aClass;

    public RemoteMethodHandler(Class aClass) {
        this.aClass = aClass;
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        Http annotation = thisMethod.getAnnotation(Http.class);
        String address = annotation.address();
        if(StringUtils.isBlank(address)) {
            log.error("address is a must");
            return null;
        }
        RemoteHeader remoteHeader = parseAddress(address);
        if(remoteHeader == null) {
            log.error("The address does not conform to the syntax. Please refer to: GET http://org.exmpale/example HTTP/1.1");
            return null;
        }
        String url = remoteHeader.getUrl();
        String method = remoteHeader.getMethod();

        try {
            HttpClient httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(3))
                    .build();

            HttpRequest httpRequest;
            if(args.length == 0) {
                httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .setHeader("reqMethod", method)
                        .build();
            } else {
                Object arg = args[0];
                String content;
                if(String.class.isAssignableFrom(arg.getClass())) {
                    content = (String) arg;
                } else {
                    content = JsonUtils.toJson(arg);
                }
                HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(content);
                httpRequest = HttpRequest.newBuilder()
                        .POST(bodyPublisher)
                        .uri(URI.create(url))
                        .setHeader("reqMethod", method)
                        .build();
            }

            HttpResponse.BodyHandler<String> stringBodyHandler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, stringBodyHandler);

            int code = httpResponse.statusCode();
            if(code != 200) {
                log.error("http server internet error: " + httpResponse.body());
                log.error(httpResponse.body());
                return null;
            }
            String body = httpResponse.body();
            return body;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private RemoteHeader parseAddress(String address) {
        if(StringUtils.isBlank(address)) {
            return null;
        }
        String[] split = address.split(StringUtils.SPACE);
        if(split.length != 3) {
            return null;
        }
        return new RemoteHeader(split[0], split[1], split[2]);
    }

    class RemoteHeader {
        private String method;
        private String url;
        private String version;
        public RemoteHeader(String method, String url, String version) {
            this.method = method;
            this.url = url;
            this.version = version;
        }

        public String getMethod() {
            return method;
        }

        public String getUrl() {
            return url;
        }

        public String getVersion() {
            return version;
        }
    }
}
