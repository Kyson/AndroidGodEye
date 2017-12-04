package cn.hikyson.godeye.monitor.server;

import android.net.Uri;
import android.text.TextUtils;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;

import cn.hikyson.godeye.core.utils.IoUtil;

public class RequestHandler {

    public RequestHandler() {
    }

    public void handle(Socket socket) throws Throwable {
        BufferedReader reader = null;
        PrintStream output = null;
        try {
            String pathAndParams = null;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while (!TextUtils.isEmpty(line = reader.readLine())) {
                if (line.startsWith("GET /")) {
                    int start = line.indexOf('/') + 1;
                    int end = line.indexOf(' ', start);
                    pathAndParams = line.substring(start, end);
                    break;
                }
            }
            output = new PrintStream(socket.getOutputStream());
            if (pathAndParams == null || pathAndParams.isEmpty()) {
                pathAndParams = "index.html";
            }
            Uri uri = parseUri(pathAndParams);
            byte[] bytes = Router.get().process(uri);
            if (null == bytes) {
                writeServerError(output);
                return;
            }
            output.println("HTTP/1.0 200 OK");
            output.println("Content-Type: " + prepareMimeType(uri.getPath()));
            output.println("Content-Length: " + bytes.length);
            output.println();
            output.write(bytes);
            output.flush();
        } finally {
            IoUtil.closeSilently(output);
            IoUtil.closeSilently(reader);
        }
    }

    private Uri parseUri(String url) throws UnsupportedEncodingException {
        return Uri.parse(URLDecoder.decode(url, "UTF-8"));
    }

    private void writeServerError(PrintStream output) {
        output.println("HTTP/1.0 500 Internal Server Error");
        output.flush();
    }

    private static String prepareMimeType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        } else if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else {
            return "application/octet-stream";
        }
    }
}
