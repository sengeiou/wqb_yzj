package com.wqb.commons;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * HttpClient工具类
 * @author FS
 * @date 2018-10-12 15:30
 */
public class HttpClientUtils {
    public static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final int TIMEOUT = 6;
    private static final int MAX_TOTAL = 200;
    private static final int MAX_PER_ROUTE = 100;

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    private static RequestConfig requestConfig;
    private static PoolingHttpClientConnectionManager connectionManager;
    private static SSLConnectionSocketFactory sslConnectionSocketFactory;
    private static RetryHandler retryHandler = new RetryHandler();

    static {
        try {
            // 全部信任 不做身份鉴定
            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] x509Certificates, String s) {
                            return true;
                        }
                    })
                    .build();

            sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"},
                    null,
                    NoopHostnameVerifier.INSTANCE);

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, PlainConnectionSocketFactory.INSTANCE)
                    .register(HTTPS, sslConnectionSocketFactory)
                    .build();

            connectionManager = new PoolingHttpClientConnectionManager(registry);
            connectionManager.setMaxTotal(MAX_TOTAL);
            connectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);

            requestConfig = RequestConfig.custom()
                    .setSocketTimeout(TIMEOUT * 1000)
                    .setConnectTimeout(TIMEOUT * 1000)
                    .setConnectionRequestTimeout(1000)
                    .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler)
                .setConnectionManager(connectionManager)
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setConnectionTimeToLive(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public static String get(String httpUrl) throws IOException {
        HttpGet httpGet = new HttpGet(httpUrl);
        return execute(httpGet);
    }

    public static String get(String httpUrl, Map<String, ?> params) throws IOException {
        String queryString = getQueryString(params);
        HttpGet httpGet = new HttpGet(queryString == null ? httpUrl : httpUrl  + queryString);
        return execute(httpGet);
    }

    public static String get(String httpUrl, Map<String, String> headers, Map<String, ?> params) throws IOException {
        String queryString = getQueryString(params);
        HttpGet httpGet = new HttpGet(queryString == null ? httpUrl : httpUrl  + queryString);
        httpGet.setHeaders(getHeaders(headers));
        return execute(httpGet);
    }

    public static String post(String httpUrl, Map<String, ?> params) throws IOException {
        HttpPost httpPost = new HttpPost(httpUrl);
        httpPost.setEntity(getPayload(params));
        return execute(httpPost);
    }

    public static String post(String httpUrl, Map<String, String> headers, Map<String, ?> params) throws IOException {
        HttpPost httpPost = new HttpPost(httpUrl);
        httpPost.setEntity(getPayload(params));
        httpPost.setHeaders(getHeaders(headers));
        return execute(httpPost);
    }

    public static String execute(HttpRequestBase request) throws IOException {
        CloseableHttpClient httpClient = getHttpClient();
        // 执行请求 会关闭连接,释放资源
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            String body = entity == null ? null : EntityUtils.toString(entity, CHARSET);

            if (statusCode ==  HttpStatus.SC_OK) {
                return body;
            }
            logger.error("错误码：{} \n 响应内容：{}", statusCode, body);
            return null;
        }
    }

    /**
     * 构建url参数
     *
     * @param params  map
     * @return  格式:key1=value1&key2=value2
     */
    public static String getQueryString(Map<String, ?> params) {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            if (StringUtils.isBlank(entry.getKey()) || StringUtils.isBlank(entry.getValue().toString())) {
                continue;
            }
            if (i++ == 0 ) {
                sb.append("?");
            } else {
                sb.append("&");
            }

            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }

        return sb.toString();
    }

    /**
     * 构建form表单参数
     *
     * @param params map
     * @return StringEntity
     */
    private static StringEntity getPayload(Map<String, ?> params) {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        List<NameValuePair> forParams  = new ArrayList<>();
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            if (StringUtils.isBlank(entry.getKey()) || entry.getValue() == null ||
                    StringUtils.isBlank(entry.getValue().toString())) {
                continue;
            }
            forParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }

        return new UrlEncodedFormEntity(forParams , CHARSET);
    }

    private static Header[] getHeaders(Map<String, String> params) {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        Set<Header> headers  = new HashSet<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (StringUtils.isBlank(entry.getKey()) || entry.getValue() == null ||
                    StringUtils.isBlank(entry.getValue())) {
                continue;
            }
            headers.add(new BasicHeader(entry.getKey(), entry.getValue()));
        }

        return headers.toArray(new Header[]{});
    }

    public static Map<String, String> commonHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
        return headers;
    }

    /**
     * http请求重试处理器
     */
    private static class RetryHandler implements HttpRequestRetryHandler {
        @Override
        public boolean retryRequest(IOException e, int executionCount, HttpContext context) {
            if (executionCount >= 5) {
                // Do not retry if over max retry count
                return false;
            }
            if (e instanceof ConnectTimeoutException) {
                // Connection refused
                return false;
            }
            if (e instanceof InterruptedIOException) {
                // Timeout
                return false;
            }
            if (e instanceof UnknownHostException) {
                // Unknown host
                return false;
            }
            if (e instanceof SSLException) {
                // SSL handshake exception
                return false;
            }

            HttpRequest request = HttpClientContext.adapt(context).getRequest();
            // Retry if the request is considered idempotent
            return !(request instanceof HttpEntityEnclosingRequest);
        }
    }
}
