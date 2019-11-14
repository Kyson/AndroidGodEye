package cn.hikyson.android.godeye.okhttp;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

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

class OkHttpNetworkContentInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    HttpContentTimeMapping mHttpContentTimeMapping;

    OkHttpNetworkContentInterceptor(HttpContentTimeMapping httpContentTimeMapping) {
        mHttpContentTimeMapping = httpContentTimeMapping;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Connection connection = chain.connection();
        HttpContent httpContent = new HttpContent();
        mHttpContentTimeMapping.addRecord(chain.call(), httpContent);
        httpContent.httpRequest.method = request.method();
        httpContent.httpRequest.url = String.valueOf(request.url());
        httpContent.httpRequest.protocol = connection != null ? String.valueOf(connection.protocol()) : "NULL";
        httpContent.httpRequest.headers = new HashMap<>();
        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            httpContent.httpRequest.headers.put(headers.name(i), headers.value(i));
        }
        if (!hasRequestBody) {
            httpContent.httpRequest.payload = "(No request body)";
        } else if (bodyHasUnknownEncoding(request.headers())) {
            httpContent.httpRequest.payload = "(Unknown encoding request body)";
        } else if (requestBody.isDuplex()) {
            httpContent.httpRequest.payload = "(duplex request body, Maybe HTTP2)";
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
                httpContent.httpRequest.payload = buffer.readString(charset) + "\n(" + requestBody.contentLength() + "-byte request body)";
            } else {
                httpContent.httpRequest.payload = "(binary " + requestBody.contentLength() + "-byte request body)";
            }
        }
        Response response;
        response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        httpContent.httpResponse.protocol = String.valueOf(response.protocol());
        httpContent.httpResponse.code = response.code();
        httpContent.httpResponse.message = response.message();
        httpContent.httpResponse.headers = new HashMap<>();
        Headers responseHeaders = response.headers();
        for (int i = 0, count = responseHeaders.size(); i < count; i++) {
            httpContent.httpResponse.headers.put(responseHeaders.name(i), responseHeaders.value(i));
        }
        if (!HttpHeaders.hasBody(response)) {
            httpContent.httpResponse.payload = "(No response body)";
        } else if (bodyHasUnknownEncoding(responseHeaders)) {
            httpContent.httpResponse.payload = "(Unknown encoding response body)";
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
                httpContent.httpResponse.payload = "(binary " + buffer.size() + "-byte response body)";
                return response;
            }
            if (contentLength != 0) {
                httpContent.httpResponse.payload = buffer.clone().readString(charset);
            }
            if (gzippedLength != null) {
                httpContent.httpResponse.payload = httpContent.httpResponse.payload + "\n(" + buffer.size() + "-byte, " + gzippedLength + "-gzipped-byte response body)";
            } else {
                httpContent.httpResponse.payload = httpContent.httpResponse.payload + "\n(" + buffer.size() + "-byte response body)";
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
