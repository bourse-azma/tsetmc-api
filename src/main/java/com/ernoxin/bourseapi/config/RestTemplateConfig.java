package com.ernoxin.bourseapi.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Base64;

@Configuration
@Slf4j
public class RestTemplateConfig {

    private static HttpClient buildTlsHttpClient(String baseUrl, long connectTimeoutMs) {
        try {
            URI uri = URI.create(baseUrl);
            String host = uri.getHost();
            int port = uri.getPort() > 0 ? uri.getPort() : 443;

            if (StringUtils.isEmpty(host)) {
                throw new IllegalArgumentException("Invalid external.tsetmc.base-url: " + baseUrl);
            }

            X509Certificate[] chain = fetchServerCertificateChain(host, port);
            KeyStore keyStore = createTrustStore(chain);
            char[] password = randomPassword();
            Path trustStorePath = saveTrustStoreToTempFile(keyStore, password);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            log.info("Created runtime truststore for {}:{} at {}", host, port, trustStorePath);

            return HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .connectTimeout(java.time.Duration.ofMillis(connectTimeoutMs))
                    .build();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to build TLS client for TSETMC upstream", ex);
        }
    }

    private static X509Certificate[] fetchServerCertificateChain(String host, int port)
            throws GeneralSecurityException, IOException {
        SSLContext insecureContext = SSLContext.getInstance("TLS");
        SavingTrustManager savingTrustManager = new SavingTrustManager();
        insecureContext.init(null, new TrustManager[]{savingTrustManager}, new SecureRandom());

        try (SSLSocket socket = (SSLSocket) insecureContext.getSocketFactory().createSocket(host, port)) {
            socket.setSoTimeout(10_000);
            socket.startHandshake();
            SSLSession session = socket.getSession();
            if (session == null) {
                throw new GeneralSecurityException("TLS handshake completed but SSL session is null");
            }
        }

        X509Certificate[] chain = savingTrustManager.getChain();
        if (chain == null || chain.length == 0) {
            throw new GeneralSecurityException("No certificate chain received from upstream");
        }

        return chain;
    }

    private static KeyStore createTrustStore(X509Certificate[] chain) throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, null);

        for (int i = 0; i < chain.length; i++) {
            keyStore.setCertificateEntry("tsetmc-cert-" + i, chain[i]);
        }

        return keyStore;
    }

    private static Path saveTrustStoreToTempFile(KeyStore keyStore, char[] password)
            throws GeneralSecurityException, IOException {
        Path trustStorePath = Files.createTempFile("tsetmc-truststore-", ".p12");
        trustStorePath.toFile().deleteOnExit();

        try (OutputStream outputStream = Files.newOutputStream(trustStorePath)) {
            keyStore.store(outputStream, password);
        }

        return trustStorePath;
    }

    private static char[] randomPassword() {
        byte[] randomBytes = new byte[24];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).toCharArray();
    }

    @Bean
    public RestTemplate tsetmcRestTemplate(
            @Value("${external.tsetmc.base-url}") String baseUrl,
            @Value("${external.tsetmc.connect-timeout-ms:10000}") long connectTimeoutMs,
            @Value("${external.tsetmc.read-timeout-ms:15000}") long readTimeoutMs) {

        JdkClientHttpRequestFactory jdkFactory = new JdkClientHttpRequestFactory(buildTlsHttpClient(baseUrl, connectTimeoutMs));
        jdkFactory.setReadTimeout(java.time.Duration.ofMillis(readTimeoutMs));

        org.springframework.http.client.ClientHttpRequestFactory factory = log.isDebugEnabled()
                ? new org.springframework.http.client.BufferingClientHttpRequestFactory(jdkFactory)
                : jdkFactory;

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
        restTemplate.getInterceptors().add(new LoggingInterceptor());
        return restTemplate;
    }

    private static final class SavingTrustManager implements X509TrustManager {
        private X509Certificate[] chain;

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
            // Not used for client-side trust bootstrap.
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
            this.chain = chain;
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        X509Certificate[] getChain() {
            return chain;
        }
    }
}
