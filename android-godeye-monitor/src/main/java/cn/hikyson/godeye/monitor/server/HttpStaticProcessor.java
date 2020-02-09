package cn.hikyson.godeye.monitor.server;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;

import java.io.IOException;
import java.io.InputStream;

import cn.hikyson.godeye.core.utils.IoUtil;

/**
 * 静态资源模块
 * Created by kysonchao on 2017/9/3.
 */
public class HttpStaticProcessor {
    private AssetManager mAssets;

    public HttpStaticProcessor(Context context) {
        mAssets = context.getResources().getAssets();
    }

    public boolean process(AsyncHttpServerRequest request, AsyncHttpServerResponse response) throws Throwable {
        String path = request.getPath();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (TextUtils.isEmpty(path)) {
            path = "index.html";
        }
        String fileName = "android-godeye-dashboard/" + path;
        StaticResource staticResource = new StaticResource(parseMimeType(fileName), loadContent(fileName, mAssets));
        response.send(staticResource.contentType, staticResource.payload);
        return true;
    }

    private static String loadContent(String fileName, AssetManager assetManager) throws IOException {
        InputStream input = null;
        try {
            input = assetManager.open(fileName);
            return IoUtil.inputStreamToString(input);
        } finally {
            IoUtil.closeSilently(input);
        }
    }

    private static String parseMimeType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        } else if (fileName.endsWith(".html")) {
            return "text/html;charset=utf-8";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript;charset=utf-8";
        } else if (fileName.endsWith(".css")) {
            return "text/css;charset=utf-8";
        } else {
            return "application/octet-stream";
        }
    }

    public static class StaticResource {
        public String contentType;
        public String payload;

        public StaticResource(String contentType, String payload) {
            this.contentType = contentType;
            this.payload = payload;
        }
    }
}
