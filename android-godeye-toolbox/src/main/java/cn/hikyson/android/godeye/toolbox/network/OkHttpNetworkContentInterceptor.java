package cn.hikyson.android.godeye.toolbox.network;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cn.hikyson.godeye.core.utils.IoUtil;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

public class OkHttpNetworkContentInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public OkHttpNetworkContentInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Connection connection = chain.connection();
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.method = request.method();
        httpRequest.url = String.valueOf(request.url());
        httpRequest.protocol = connection != null ? String.valueOf(connection.protocol()) : "NULL";
        String requestLine = request.method() + ' ' + request.url() + (connection != null ? " " + connection.protocol() : "");
        httpRequest.headers = new HashMap<>();
        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            httpRequest.headers.put(headers.name(i), headers.value(i));
        }
        if (!hasRequestBody) {
            httpRequest.payload = "(No request body)";
        } else if (bodyHasUnknownEncoding(request.headers())) {
            httpRequest.payload = "(Unknown encoding request body)";
        } else if (requestBody.isDuplex()) {
            httpRequest.payload = "(duplex request body, Maybe HTTP2)";
        } else {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = null;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (charset == null) {
                charset = UTF8;
            }
            if (isPlaintext(buffer)) {
                httpRequest.payload = buffer.readString(charset) + "\n(" + requestBody.contentLength() + "-byte request body)";
            } else {
                httpRequest.payload = "(binary " + requestBody.contentLength() + "-byte request body)";
            }
        }
        HttpResponse httpResponse = new HttpResponse();
        boolean isSuccessful = true;
        String message = "";
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            isSuccessful = false;
            message = String.valueOf(e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        httpResponse.protocol = String.valueOf(response.protocol());
        httpResponse.code = response.code();
        httpResponse.message = response.message();
        httpResponse.headers = new HashMap<>();
        Headers responseHeaders = response.headers();
        for (int i = 0, count = responseHeaders.size(); i < count; i++) {
            httpResponse.headers.put(responseHeaders.name(i), responseHeaders.value(i));
        }
        if (!HttpHeaders.hasBody(response)) {
            httpResponse.payload = "(No response body)";
        } else if (bodyHasUnknownEncoding(responseHeaders)) {
            httpResponse.payload = "(Unknown encoding response body)";
        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.getBuffer();
            Long gzippedLength = null;
            if ("gzip".equalsIgnoreCase(responseHeaders.get("Content-Encoding"))) {
                gzippedLength = buffer.size();
                GzipSource gzippedResponseBody = null;
                try {
                    gzippedResponseBody = new GzipSource(buffer.clone());
                    buffer = new Buffer();
                    buffer.writeAll(gzippedResponseBody);
                } finally {
                    IoUtil.closeSilently(gzippedResponseBody);
                }
            }
            Charset charset = null;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (charset == null) {
                charset = UTF8;
            }
            if (!isPlaintext(buffer)) {
                httpResponse.payload = "(binary " + buffer.size() + "-byte response body)";
                return response;
            }
            if (contentLength != 0) {
                httpResponse.payload = buffer.clone().readString(charset);
            }
            if (gzippedLength != null) {
                httpResponse.payload = httpResponse.payload + "\n(" + buffer.size() + "-byte, " + gzippedLength + "-gzipped-byte response body)";
            } else {
                httpResponse.payload = httpResponse.payload + "\n(" + buffer.size() + "-byte response body)";
            }
        }
        return response;
    }

    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private static boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
                && !contentEncoding.equalsIgnoreCase("identity")
                && !contentEncoding.equalsIgnoreCase("gzip");
    }
}
